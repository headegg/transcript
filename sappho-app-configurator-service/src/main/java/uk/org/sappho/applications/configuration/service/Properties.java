/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Properties {

    private WorkingCopy workingCopy;

    @Inject
    public Properties(WorkingCopy workingCopy) throws ConfigurationException {

        this.workingCopy = workingCopy;
    }

    public Map<String, String> getAll(String environment, String application) throws ConfigurationException {

        Map<String, String> properties = getRawProperties(environment, application);
        Map<String, String> versionControlProperties =
                workingCopy.getVersionControlProperties(jsonFilename(environment, application));
        for (String key : versionControlProperties.keySet()) {
            properties.put(key, versionControlProperties.get(key));
        }
        return properties;
    }

    public String get(String environment, String application, String key) throws ConfigurationException {

        String value = getAll(environment, application).get(key);
        if (value == null) {
            throw new ConfigurationException("No value for " + key);
        }
        return value;
    }

    public void put(String environment, String application, Map<String, String> properties)
            throws ConfigurationException {

        getRawProperties(environment, application);
        commitAllProperties(environment, application, properties);
    }

    public void put(String environment, String application, String key, String value) throws ConfigurationException {

        Map<String, String> properties = getRawProperties(environment, application);
        if (value != null) {
            properties.put(key, value);
        } else {
            if (properties.containsKey(key)) {
                properties.remove(key);
            }
        }
        commitAllProperties(environment, application, properties);
    }

    public void delete(String environment, String application, String key) throws ConfigurationException {

        put(environment, application, key, null);
    }

    public void delete(String environment, String application) throws ConfigurationException {

        put(environment, application, new LinkedHashMap<String, String>());
    }

    private String jsonFilename(String environment, String application) {

        return environment + "/" + application + ".json";
    }

    private Map<String, String> getRawProperties(String environment, String application) throws ConfigurationException {

        try {
            File file = workingCopy.getUpToDateFile(jsonFilename(environment, application));
            Map<String, String> properties = new LinkedHashMap<String, String>();
            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                properties = new Gson().fromJson(fileReader, LinkedHashMap.class);
                fileReader.close();
            } else {
                putAllProperties(environment, application, properties);
            }
            return properties;
        } catch (Throwable throwable) {
            throw new ConfigurationException("Unable to read " + jsonFilename(environment, application), throwable);
        }
    }

    private void commitAllProperties(String environment, String application, Map<String, String> properties)
            throws ConfigurationException {

        putAllProperties(environment, application, properties);
        workingCopy.commit(jsonFilename(environment, application));
    }

    private void putAllProperties(String environment, String application, Map<String, String> properties)
            throws ConfigurationException {

        try {
            File directory = workingCopy.getFile(environment);
            if (!directory.exists()) {
                directory.mkdir();
            }
            JsonWriter jsonWriter =
                    new JsonWriter(new FileWriter(workingCopy.getFile(jsonFilename(environment, application))));
            jsonWriter.setIndent("    ");
            jsonWriter.setHtmlSafe(true);
            new Gson().toJson(properties, LinkedHashMap.class, jsonWriter);
            jsonWriter.close();
        } catch (Throwable throwable) {
            throw new ConfigurationException("Unable to write " + jsonFilename(environment, application), throwable);
        }
    }
}
