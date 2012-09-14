/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.devops.service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.codehaus.plexus.util.FileUtils;
import uk.org.sappho.applications.devops.service.vcs.VersionControlSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WorkingCopy {

    private String workingCopyPath;
    private String workingCopyId;
    private String environment;
    private String application;
    private String jsonFilename;
    private VersionControlSystem versionControlSystem;
    private Object lock;

    @Inject
    public WorkingCopy(@Named("working.copy.path") String workingCopyPath,
                       @Named("working.copy.id") String workingCopyId,
                       @Named("environment") String environment,
                       @Named("application") String application,
                       VersionControlSystem versionControlSystem,
                       WorkingCopySynchronizer workingCopySynchronizer) throws ConfigurationException {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.environment = environment;
        this.application = application;
        jsonFilename = environment + "/" + application + ".json";
        this.versionControlSystem = versionControlSystem;
        lock = workingCopySynchronizer.getLock(workingCopyId);
    }

    public File getUpToDatePath(String path) throws ConfigurationException {

        synchronized (lock) {
            File workingCopy = new File(workingCopyPath, workingCopyId);
            if (workingCopy.exists()) {
                if (!workingCopy.isDirectory()) {
                    throw new ConfigurationException("Requested working copy " + workingCopyId +
                            " is not a directory");
                }
                versionControlSystem.update(path);
            } else {
                versionControlSystem.checkout();
            }
            return new File(workingCopy, path);
        }
    }

    public Map<String, String> getPropertiesFromFile(boolean includeVersionControlProperties)
            throws ConfigurationException {

        synchronized (lock) {
            try {
                File jsonFile = getUpToDatePath(jsonFilename);
                Map<String, String> properties = new LinkedHashMap<String, String>();
                if (jsonFile.exists()) {
                    FileReader fileReader = new FileReader(jsonFile);
                    properties = new Gson().fromJson(fileReader, LinkedHashMap.class);
                    fileReader.close();
                    if (properties == null) {
                        properties = new LinkedHashMap<String, String>();
                    }
                    if (includeVersionControlProperties) {
                        Map<String, String> versionControlProperties =
                                versionControlSystem.getProperties(jsonFilename);
                        for (String key : versionControlProperties.keySet()) {
                            properties.put(key, versionControlProperties.get(key));
                        }
                    }
                }
                return properties;
            } catch (Throwable throwable) {
                throw new ConfigurationException("Unable to read " + jsonFilename, throwable);
            }
        }
    }

    public void putPropertiesToFile(Map<String, String> properties) throws ConfigurationException {

        synchronized (lock) {
            File jsonFile = getUpToDatePath(jsonFilename);
            File directory = new File(new File(workingCopyPath, workingCopyId), environment);
            boolean isNewDirectory = !directory.exists();
            boolean isNewFile = !jsonFile.exists();
            try {
                if (isNewDirectory) {
                    if (!directory.mkdir()) {
                        throw new IOException("Unable to create new environment " + environment);
                    }
                } else {
                    if (!directory.isDirectory()) {
                        throw new IOException("Unable to create new environment " + environment);
                    }
                }
                JsonWriter jsonWriter = new JsonWriter(new FileWriter(jsonFile));
                jsonWriter.setIndent("    ");
                jsonWriter.setHtmlSafe(true);
                new Gson().toJson(properties, LinkedHashMap.class, jsonWriter);
                jsonWriter.close();
                if (isNewDirectory) {
                    versionControlSystem.commit(environment, true);
                } else {
                    versionControlSystem.commit(jsonFilename, isNewFile);
                }
            } catch (Throwable throwable) {
                if (isNewFile) {
                    try {
                        FileUtils.forceDelete(isNewDirectory ? directory : jsonFile);
                    } catch (Throwable deleteException) {
                    }
                }
                throw new ConfigurationException("Unable to write " + jsonFilename, throwable);
            }
        }
    }

    public void delete() throws ConfigurationException {

        synchronized (lock) {
            versionControlSystem.delete(jsonFilename);
        }
    }
}
