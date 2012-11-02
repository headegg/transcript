/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.reporter;

import com.google.gson.internal.StringMap;
import com.google.inject.Inject;
import uk.org.sappho.applications.services.transcript.registry.Applications;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.Environments;
import uk.org.sappho.applications.services.transcript.registry.Properties;
import uk.org.sappho.applications.services.transcript.registry.WorkingCopy;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReportDataSource {

    private Environments environments;
    private Applications applications;
    private Properties properties;
    private WorkingCopy workingCopy;
    private StringMap dictionary;
    private String[] requiredEnvironments = new String[0];
    private String[] requiredApplications = new String[0];
    private String[] requiredKeys = new String[0];
    private boolean includeVersionControlProperties;
    private boolean includeUndefinedEnvironments;
    private Set<String> reportableEnvironments = null;
    private Set<String> reportableApplications = null;
    private Set<String> reportableKeys = null;
    private Map<String, Map<String, Map<String, String>>> cache =
            new TreeMap<String, Map<String, Map<String, String>>>();

    @Inject
    public ReportDataSource(Environments environments,
                            Applications applications,
                            Properties properties,
                            WorkingCopy workingCopy) {

        this.environments = environments;
        this.applications = applications;
        this.properties = properties;
        this.workingCopy = workingCopy;
    }

    public void loadDictionary() throws ConfigurationException {

        dictionary = workingCopy.getProperties(".devops", ".dictionary");
    }

    public StringMap getDictionaryMap(String key) {

        return (StringMap) dictionary.get(key);
    }

    public String[] getRequiredEnvironments() {

        return requiredEnvironments;
    }

    public void setRequiredEnvironments(String[] requiredEnvironments) {

        this.requiredEnvironments = requiredEnvironments;
    }

    public String[] getRequiredApplications() {

        return requiredApplications;
    }

    public void setRequiredApplications(String[] requiredApplications) {

        this.requiredApplications = requiredApplications;
    }

    public String getEnvironment() {

        String environment = null;
        if (requiredEnvironments != null && requiredEnvironments.length == 1) {
            environment = requiredEnvironments[0];
        }
        return environment;
    }

    public String getApplication() {

        String application = null;
        if (requiredApplications != null && requiredApplications.length == 1) {
            application = requiredApplications[0];
        }
        return application;
    }

    public String[] getRequiredKeys() {

        return requiredKeys;
    }

    public void setRequiredKeys(String[] requiredKeys) {

        this.requiredKeys = requiredKeys;
    }

    public void setIncludeUndefinedEnvironments(boolean includeUndefinedEnvironments) {

        this.includeUndefinedEnvironments = includeUndefinedEnvironments;
    }

    public void setIncludeVersionControlProperties(boolean includeVersionControlProperties) {

        this.includeVersionControlProperties = includeVersionControlProperties;
    }

    public Set<String> getReportableEnvironments() throws ConfigurationException {

        if (reportableEnvironments == null) {
            reportableEnvironments =
                    environments.getEnvironmentNames(requiredEnvironments, getApplication(),
                            includeUndefinedEnvironments);
        }
        return reportableEnvironments;
    }

    public Set<String> getReportableApplications() throws ConfigurationException {

        if (reportableApplications == null) {
            reportableApplications =
                    applications.getApplicationNames(getReportableEnvironments(), requiredApplications);
        }
        return reportableApplications;
    }

    public Set<String> getReportableKeys() throws ConfigurationException {

        if (reportableKeys == null) {
            Map<String, String> keys = new TreeMap<String, String>();
            for (String environment : getReportableEnvironments()) {
                for (String application : getReportableApplications()) {
                    Map<String, String> properties = getProperties(environment, application);
                    for (String key : properties.keySet()) {
                        keys.put(key, key);
                    }
                }
            }
            reportableKeys = keys.keySet();
        }
        return reportableKeys;
    }

    public Map<String, String> getProperties(String environment, String application) {

        Map<String, Map<String, String>> environmentCache = cache.get(environment);
        if (environmentCache == null) {
            environmentCache = new TreeMap<String, Map<String, String>>();
            cache.put(environment, environmentCache);
        }
        Map<String, String> propertyCache = environmentCache.get(application);
        if (propertyCache == null) {
            try {
                propertyCache = properties.getAllProperties(environment, application, includeVersionControlProperties);
            } catch (Throwable throwable) {
            }
            if (propertyCache == null) {
                propertyCache = new TreeMap<String, String>();
            }
            environmentCache.put(application, propertyCache);
        }
        return propertyCache;
    }

    public PropertyValue getProperty(String environment, String application, String key) {

        String value = null;
        Map<String, String> properties = getProperties(environment, application);
        if (properties != null && properties.containsKey(key)) {
            value = properties.get(key);
        }
        return new PropertyValue(value);
    }
}
