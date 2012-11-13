/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.codehaus.plexus.util.FileUtils;
import uk.org.sappho.applications.transcript.service.vcs.VersionControlSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Singleton
public class WorkingCopy {

    private final TranscriptParameters transcriptParameters;
    private final VersionControlSystem versionControlSystem;
    private final static Map<String, Object> LOCKS = new HashMap<String, Object>();

    @Inject
    public WorkingCopy(TranscriptParameters transcriptParameters,
                       VersionControlSystem versionControlSystem) {

        this.transcriptParameters = transcriptParameters;
        this.versionControlSystem = versionControlSystem;
    }

    public File getUpToDatePath(String path) throws ConfigurationException {

        File workingCopy = new File(transcriptParameters.getWorkingCopyPath(), transcriptParameters.getWorkingCopyId());
        synchronized (getLock()) {
            if (workingCopy.exists()) {
                if (!workingCopy.isDirectory()) {
                    throw new ConfigurationException("Requested working copy " +
                            transcriptParameters.getWorkingCopyId() + " is not a directory");
                }
                if (!transcriptParameters.isUseCache()) {
                    versionControlSystem.update(path);
                }
            } else {
                versionControlSystem.checkout();
            }
        }
        return new File(workingCopy, path);
    }

    public SortedMap<String, String> getStringProperties(String environment, String application)
            throws ConfigurationException {

        return (SortedMap<String, String>) getProperties(environment, application, new TreeMap<String, String>());
    }

    public StringMap getPropertyTree(String environment, String application) throws ConfigurationException {

        return (StringMap) getProperties(environment, application, new StringMap());
    }

    private Map getProperties(String environment, String application, Map emptyProperties)
            throws ConfigurationException {

        Map properties = emptyProperties;
        try {
            synchronized (getLock()) {
                File jsonFile = getUpToDatePath(getJsonFilename(environment, application));
                if (jsonFile.exists()) {
                    FileReader fileReader = new FileReader(jsonFile);
                    try {
                        properties = new Gson().fromJson(fileReader, emptyProperties.getClass());
                    } finally {
                        try {
                            fileReader.close();
                        } catch (Throwable throwable) {
                        }
                    }
                }
                if (transcriptParameters.isIncludeVersionControlProperties()) {
                    Map<String, String> versionControlProperties =
                            versionControlSystem.getProperties(getJsonFilename(environment, application));
                    for (String key : versionControlProperties.keySet()) {
                        properties.put(key, versionControlProperties.get(key));
                    }
                }
            }
        } catch (Throwable throwable) {
            throw new ConfigurationException("Unable to read " + getJsonFilename(environment, application), throwable);
        }
        return properties;
    }

    public void putProperties(String environment, String application, SortedMap<String, String> properties)
            throws ConfigurationException {

        checkWritable();
        synchronized (getLock()) {
            File jsonFile = getUpToDatePath(getJsonFilename(environment, application));
            File directory = new File(new File(transcriptParameters.getWorkingCopyPath(),
                    transcriptParameters.getWorkingCopyId()), environment);
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
    }

    public void deleteProperties(String environment, String application) throws ConfigurationException {

        checkWritable();
        String filename = getJsonFilename(environment, application);
        synchronized (getLock()) {
            getUpToDatePath(filename);
            versionControlSystem.delete(filename);
        }
    }

    private void checkWritable() throws ConfigurationException {

        if (transcriptParameters.isReadOnly()) {
            throw new ConfigurationException("Working copy " + transcriptParameters.getWorkingCopyId() +
                    " is read only");
        }
    }

    private String getJsonFilename(String environment, String application) {

        return environment + "/" + application + ".json";
    }

    private void unableToCreateNewEnvironment(String environment) throws IOException {

        throw new IOException("Unable to create new environment " + environment);
    }

    private Object getLock() {

        synchronized (LOCKS) {
            Object lock = LOCKS.get(transcriptParameters.getWorkingCopyId());
            if (lock == null) {
                lock = new Object();
                LOCKS.put(transcriptParameters.getWorkingCopyId(), lock);
            }
            return lock;
        }
    }
}
