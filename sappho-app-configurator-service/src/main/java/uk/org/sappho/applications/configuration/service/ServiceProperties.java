/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ServiceProperties {

    private final Map<String, String> properties = new HashMap<String, String>();

    public void put(String key, List<String> values) {

        String value = null;
        if (values != null && values.size() > 0) {
            value = values.get(values.size() - 1);
        }
        properties.put(key, value);
    }

    public Map<String, String> getProperties() {

        return properties;
    }
}
