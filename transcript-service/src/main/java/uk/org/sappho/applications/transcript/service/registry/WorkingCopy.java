/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.TranscriptParameters;
import uk.org.sappho.applications.transcript.service.registry.vcs.VersionControlSystem;

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

    public File getUpToDatePath(String path) throws TranscriptException {

        File workingCopy = new File(transcriptParameters.getWorkingCopyPath(), transcriptParameters.getWorkingCopyId());
        synchronized (getLock()) {
            if (workingCopy.exists()) {
                if (!workingCopy.isDirectory()) {
                    throw new TranscriptException("Requested working copy " +
                            transcriptParameters.getWorkingCopyId() + " is not a directory");
                }
                versionControlSystem.update(path);
            } else {
                File baseDirectory = new File(transcriptParameters.getWorkingCopyPath());
                if (!baseDirectory.exists()) {
                    try {
                        FileUtils.forceMkdir(baseDirectory);
                    } catch (Throwable throwable) {
                        throw new TranscriptException("Unable to create base directory for working copies", throwable);
                    }
                }
                versionControlSystem.checkout();
            }
        }
        return new File(workingCopy, path);
    }

    public void clearUpdateCache() {

        synchronized (getLock()) {
            versionControlSystem.clearUpdateCache();
        }
    }

    private SortedMap<String, Object> getJsonProperties(String environment, String application)
            throws TranscriptException {

        SortedMap<String, Object> properties = new TreeMap<String, Object>();
        synchronized (getLock()) {
            try {
                File jsonFile = getUpToDatePath(getJsonFilename(environment, application));
                if (jsonFile.exists()) {
                    FileReader fileReader = new FileReader(jsonFile);
                    try {
                        properties = new Gson().fromJson(fileReader, properties.getClass());
                    } finally {
                        try {
                            fileReader.close();
                        } catch (Throwable throwable) {
                        }
                    }
                }
            } catch (Throwable throwable) {
                throw new TranscriptException("Unable to read " + getJsonFilename(environment, application), throwable);
            }
        }
        return properties;
    }

    public SortedMap<String, Object> getProperties(String environment, String application)
            throws TranscriptException {

        return getProperties(environment, application, transcriptParameters.isIncludeVersionControlProperties());
    }

    public SortedMap<String, Object> getProperties(String environment, String application,
                                                   boolean includeVersionControlProperties)
            throws TranscriptException {

        SortedMap<String, Object> properties;
        synchronized (getLock()) {
            properties = getJsonProperties(environment, application);
            if (includeVersionControlProperties) {
                Map<String, String> versionControlProperties =
                        versionControlSystem.getProperties(getJsonFilename(environment, application));
                for (String key : versionControlProperties.keySet()) {
                    properties.put(key, versionControlProperties.get(key));
                }
            }
        }
        return properties;
    }

    private void putProperties(String environment, String application, SortedMap<String, Object> properties)
            throws TranscriptException {

        synchronized (getLock()) {
            checkWritable();
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
                throw new TranscriptException("Unable to write " + getJsonFilename(environment, application), throwable);
            }
        }
    }

    public void putProperties(String environment, String application, SortedMap<String, Object> properties,
                              boolean isMerge) throws TranscriptException {

        Gson gson = new Gson();
        synchronized (getLock()) {
            SortedMap<String, Object> oldProperties = getJsonProperties(environment, application);
            SortedMap<String, Object> newProperties;
            if (isMerge && !oldProperties.isEmpty()) {
                newProperties = new TreeMap<String, Object>();
                for (String key : oldProperties.keySet()) {
                    newProperties.put(key, oldProperties.get(key));
                }
                for (String key : properties.keySet()) {
                    Object newValue = properties.get(key);
                    if (transcriptParameters.isFailOnValueChange() && oldProperties.containsKey(key) &&
                            !gson.toJson(newValue).equals(gson.toJson(oldProperties.get(key)))) {
                        throw new TranscriptException("Value of property " +
                                environment + ":" + application + ":" + key + " would change");
                    }
                    newProperties.put(key, newValue);
                }
            } else {
                newProperties = properties;
            }
            String oldJson = gson.toJson(oldProperties);
            String newJson = gson.toJson(newProperties);
            if (!newJson.equals(oldJson)) {
                putProperties(environment, application, newProperties);
            }
        }
    }

    public void deleteProperty(String environment, String application, String key) throws TranscriptException {

        synchronized (getLock()) {
            SortedMap<String, Object> properties = getJsonProperties(environment, application);
            if (properties.containsKey(key)) {
                properties.remove(key);
                putProperties(environment, application, properties);
            }
        }
    }

    public void deleteProperties(String environment, String application) throws TranscriptException {

        checkWritable();
        String filename = getJsonFilename(environment, application);
        synchronized (getLock()) {
            getUpToDatePath(filename);
            versionControlSystem.delete(filename);
        }
    }

    private void checkWritable() throws TranscriptException {

        if (transcriptParameters.isReadOnly()) {
            throw new TranscriptException("Working copy " + transcriptParameters.getWorkingCopyId() +
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
