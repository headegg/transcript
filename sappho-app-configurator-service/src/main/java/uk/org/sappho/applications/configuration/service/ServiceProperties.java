/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.Singleton;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ServiceProperties {

    private final Map<String, String> properties = new HashMap<String, String>();
    private File workingCopyBase;
    private String workingCopyId;

    public ServiceProperties() throws ConfigurationException {

        String workingCopyPath = System.getProperty("working.copy.path");
        if (workingCopyPath == null || workingCopyPath.length() == 0) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        workingCopyBase = new File(workingCopyPath);
        workingCopyId = properties.get("workingCopyId");
        if (workingCopyId == null || workingCopyId.length() == 0) {
            workingCopyId = "default";
        }
    }

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

    public File getWorkingCopyBase() {
        return workingCopyBase;
    }

    public String getWorkingCopyId() {
        return workingCopyId;
    }
}
