/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

import com.google.inject.Inject;

import java.util.SortedMap;

public class Properties {

    private WorkingCopy workingCopy;

    @Inject
    public Properties(WorkingCopy workingCopy) throws ConfigurationException {

        this.workingCopy = workingCopy;
    }

    public SortedMap<String, String> getAllProperties(String environment, String application)
            throws ConfigurationException {

        return workingCopy.getStringProperties(environment, application);
    }

    public String get(String environment, String application, String key) throws ConfigurationException {

        String value = getAllProperties(environment, application).get(key);
        if (value == null) {
            throw new ConfigurationException("There is no value for " + key);
        }
        return value;
    }

    public void put(String environment, String application, SortedMap<String, String> properties)
            throws ConfigurationException {

        workingCopy.putProperties(environment, application, properties);
    }

    public void put(String environment, String application, String key, String value)
            throws ConfigurationException {

        SortedMap<String, String> properties = workingCopy.getStringProperties(environment, application);
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
            workingCopy.putProperties(environment, application, properties);
        }
    }

    public void delete(String environment, String application, String key) throws ConfigurationException {

        put(environment, application, key, null);
    }

    public void delete(String environment, String application) throws ConfigurationException {

        workingCopy.deleteProperties(environment, application);
    }
}
