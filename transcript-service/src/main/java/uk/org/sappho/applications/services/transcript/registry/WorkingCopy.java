/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.codehaus.plexus.util.FileUtils;
import uk.org.sappho.applications.services.transcript.registry.vcs.VersionControlSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Singleton
public class WorkingCopy {

    private String workingCopyPath;
    private String workingCopyId;
    private boolean useCache;
    private boolean readOnly;
    private VersionControlSystem versionControlSystem;

    @Inject
    public WorkingCopy(@Named("working.copy.path") String workingCopyPath,
                       @Named("working.copy.id") String workingCopyId,
                       @Named("use.cache") String useCache,
                       @Named("read.only") String readOnly,
                       VersionControlSystem versionControlSystem) throws ConfigurationException {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.useCache = useCache.equals("true");
        this.readOnly = readOnly.equals("true");
        this.versionControlSystem = versionControlSystem;
    }

    public File getUpToDatePath(String path) throws ConfigurationException {

        File workingCopy = new File(workingCopyPath, workingCopyId);
        if (workingCopy.exists()) {
            if (!workingCopy.isDirectory()) {
                throw new ConfigurationException("Requested working copy " + workingCopyId +
                        " is not a directory");
            }
            if (!useCache) {
                versionControlSystem.update(path);
            }
        } else {
            versionControlSystem.checkout();
        }
        return new File(workingCopy, path);
    }

    public SortedMap<String, String> getProperties(String environment, String application,
                                                   boolean includeVersionControlProperties)
            throws ConfigurationException {

        try {
            File jsonFile = getUpToDatePath(getJsonFilename(environment, application));
            SortedMap<String, String> properties = new TreeMap<String, String>();
            if (jsonFile.exists()) {
                FileReader fileReader = new FileReader(jsonFile);
                try {
                    properties = new Gson().fromJson(fileReader, TreeMap.class);
                } finally {
                    try {
                        fileReader.close();
                    } catch (Throwable throwable) {
                    }
                }
                if (properties == null) {
                    properties = new TreeMap<String, String>();
                }
                if (includeVersionControlProperties) {
                    Map<String, String> versionControlProperties =
                            versionControlSystem.getProperties(getJsonFilename(environment, application));
                    for (String key : versionControlProperties.keySet()) {
                        properties.put(key, versionControlProperties.get(key));
                    }
                }
            }
            return properties;
        } catch (Throwable throwable) {
            throw new ConfigurationException("Unable to read " + getJsonFilename(environment, application), throwable);
        }
    }

    public void putProperties(String environment, String application, SortedMap<String, String> properties)
            throws ConfigurationException {

        checkReadOnly();
        File jsonFile = getUpToDatePath(getJsonFilename(environment, application));
        File directory = new File(new File(workingCopyPath, workingCopyId), environment);
        boolean isNewDirectory = !directory.exists();
        boolean isNewFile = !jsonFile.exists();
        try {
            if (isNewDirectory) {
                if (!directory.mkdir()) {
                    unableToCreateNewEnvironment(environment);
                }
            } else {
                if (!directory.isDirectory()) {
                    unableToCreateNewEnvironment(environment);
                }
            }
            JsonWriter jsonWriter = new JsonWriter(new FileWriter(jsonFile));
            try {
                jsonWriter.setIndent("    ");
                jsonWriter.setHtmlSafe(true);
                new Gson().toJson(properties, properties.getClass(), jsonWriter);
                jsonWriter.close();
            } finally {
                try {
                    jsonWriter.close();
                } catch (Throwable closeException) {
                }
            }
            if (isNewDirectory) {
                versionControlSystem.commit(environment, true);
            } else {
                versionControlSystem.commit(getJsonFilename(environment, application), isNewFile);
            }
        } catch (Throwable throwable) {
            if (isNewFile) {
                try {
                    FileUtils.forceDelete(isNewDirectory ? directory : jsonFile);
                } catch (Throwable deleteException) {
                }
            }
            throw new ConfigurationException("Unable to write " + getJsonFilename(environment, application), throwable);
        }
    }

    public void deleteProperties(String environment, String application) throws ConfigurationException {

        checkReadOnly();
        versionControlSystem.delete(getJsonFilename(environment, application));
    }

    public void checkReadOnly() throws ConfigurationException {

        if (readOnly) {
            throw new ConfigurationException("Working copy " + workingCopyId + " is read only");
        }
    }

    private String getJsonFilename(String environment, String application) {

        return environment + "/" + application + ".json";
    }

    private void unableToCreateNewEnvironment(String environment) throws IOException {

        throw new IOException("Unable to create new environment " + environment);
    }
}
