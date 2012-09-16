/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry;

import com.google.inject.Inject;

import java.util.Map;

public class Properties {

    private WorkingCopy workingCopy;

    @Inject
    public Properties(WorkingCopy workingCopy) throws ConfigurationException {

        this.workingCopy = workingCopy;
    }

    public Map<String, String> getAll() throws ConfigurationException {

        return workingCopy.getPropertiesFromFile(true);
    }

    public String get(String key) throws ConfigurationException {

        String value = getAll().get(key);
        if (value == null) {
            throw new ConfigurationException("There is no value for " + key);
        }
        return value;
    }

    public void put(Map<String, String> properties) throws ConfigurationException {

        workingCopy.putPropertiesToFile(properties);
    }

    public void put(String key, String value) throws ConfigurationException {

        Map<String, String> properties = workingCopy.getPropertiesFromFile(false);
        boolean changed = false;
        if (value != null) {
            String oldValue = properties.get(key);
            properties.put(key, value);
            changed = oldValue == null || !oldValue.equals(value);
        } else {
            if (properties.containsKey(key)) {
                properties.remove(key);
                changed = true;
            }
        }
        if (changed) {
            workingCopy.putPropertiesToFile(properties);
        }
    }

    public void delete(String key) throws ConfigurationException {

        put(key, null);
    }

    public void delete() throws ConfigurationException {

        workingCopy.delete();
    }
}
