/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Properties {

    private final String workingCopyId;
    private String jsonFilename;
    private WorkingCopy workingCopy = WorkingCopy.getInstance();

    public Properties(String workingCopyId, String environment, String application) throws ConfigurationException {

        this.workingCopyId = workingCopyId;
        jsonFilename = environment + "/" + application + ".json";
    }

    public Map<String, String> getAll() throws ConfigurationException {

        Map<String, String> properties;
        try {
            Map<String, String> workingCopyProperties = new HashMap<String, String>();
            properties = new Gson().fromJson(new FileReader(workingCopy.getFile(workingCopyId, jsonFilename, workingCopyProperties)), HashMap.class);
            for (String propertyKey : workingCopyProperties.keySet()) {
                properties.put(propertyKey, workingCopyProperties.get(propertyKey));
            }
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
