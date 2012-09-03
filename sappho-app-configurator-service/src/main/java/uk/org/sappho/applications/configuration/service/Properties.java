/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.gson.Gson;
import com.google.inject.Inject;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Properties {

    private WorkingCopy workingCopy;

    @Inject
    public Properties(WorkingCopy workingCopy) throws ConfigurationException {

        this.workingCopy = workingCopy;
    }

    public Map<String, String> getAll(String environment, String application) throws ConfigurationException {

        String jsonFilename = environment + "/" + application + ".json";
        Map<String, String> properties;
        try {
            Map<String, String> workingCopyProperties = new HashMap<String, String>();
            properties = new Gson().fromJson(new FileReader(workingCopy.getFile(jsonFilename, workingCopyProperties)), HashMap.class);
            for (String propertyKey : workingCopyProperties.keySet()) {
                properties.put(propertyKey, workingCopyProperties.get(propertyKey));
            }
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to read " + jsonFilename, exception);
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
}
