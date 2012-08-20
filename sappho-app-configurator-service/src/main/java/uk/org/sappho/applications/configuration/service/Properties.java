package uk.org.sappho.applications.configuration.service;

import com.google.gson.Gson;
import sun.security.krb5.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class Properties extends WorkingCopy {

    private String jsonFilename;

    class NameValue {

        private Map<String, String> nameValuePairs;

        public Map<String, String> getNameValuePairs() {

            return nameValuePairs;
        }
    }

    public Properties(String environment, String application) throws ConfigurationException {

        super();
        jsonFilename = environment + "/" + application + ".json";
        versionedWorkingCopy.update(jsonFilename);
    }

    public Map<String, String> getAll() throws ConfigurationException {

        Map<String, String> properties;
        try {
            properties = new Gson().fromJson(new FileReader(new File(directory, jsonFilename)), HashMap.class);
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to read " + jsonFilename, exception);
        }
        return properties;
    }

    public String get(String key) throws ConfigurationException {

        String value = getAll().get(key);
        if (value == null) {
            throw new ConfigurationException("No value for " + key);
        }
        return value;
    }
}
