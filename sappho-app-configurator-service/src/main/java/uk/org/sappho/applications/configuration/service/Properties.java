package uk.org.sappho.applications.configuration.service;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Properties {

    private String jsonFilename;
    private WorkingCopy workingCopy = WorkingCopy.getInstance();

    public Properties(String environment, String application) throws ConfigurationException {

        jsonFilename = environment + "/" + application + ".json";
    }

    public Map<String, String> getAll() throws ConfigurationException {

        Map<String, String> properties;
        try {
            WorkingCopyContext workingCopyContext = new WorkingCopyContext();
            properties = new Gson().fromJson(new FileReader(workingCopy.getFile(jsonFilename, workingCopyContext)), HashMap.class);
            properties.put("vcs.head.revision", workingCopyContext.getHeadRevision());
            properties.put("vcs.repository", workingCopyContext.getRepository());
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
