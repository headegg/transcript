/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report;

import com.google.gson.internal.StringMap;
import com.google.inject.Inject;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.registry.Applications;
import uk.org.sappho.applications.transcript.service.registry.Environments;
import uk.org.sappho.applications.transcript.service.registry.Properties;
import uk.org.sappho.applications.transcript.service.registry.TranscriptParameters;
import uk.org.sappho.applications.transcript.service.registry.WorkingCopy;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReportData {

    private final Environments environments;
    private final Applications applications;
    private final Properties properties;
    private final WorkingCopy workingCopy;
    private final TranscriptParameters parameters;
    private StringMap dictionary = null;
    private Set<String> reportableEnvironments = null;
    private Set<String> reportableApplications = null;
    private Set<String> reportableKeys = null;
    private final Map<String, Map<String, Map<String, String>>> cache =
            new TreeMap<String, Map<String, Map<String, String>>>();

    @Inject
    public ReportData(Environments environments,
                      Applications applications,
                      Properties properties,
                      WorkingCopy workingCopy,
                      TranscriptParameters parameters) {

        this.environments = environments;
        this.applications = applications;
        this.properties = properties;
        this.workingCopy = workingCopy;
        this.parameters = parameters;
    }

    public TranscriptParameters getParameters() {

        return parameters;
    }

    public StringMap getDictionary() throws TranscriptException {

        if (dictionary == null) {
            dictionary = workingCopy.getPropertyTree(parameters.getDictionaryEnvironment(),
                    parameters.getDictionaryApplication());
        }
        return dictionary;
    }

    public Set<String> getReportableEnvironments() throws TranscriptException {

        if (reportableEnvironments == null) {
            reportableEnvironments =
                    environments.getEnvironmentNames(parameters.getEnvironments(),
                            parameters.getApplication(),
                            parameters.isIncludeUndefinedEnvironments());
        }
        return reportableEnvironments;
    }

    public void resetReportableEnvironments() {

        reportableEnvironments = null;
    }

    public Set<String> getReportableApplications() throws TranscriptException {

        if (reportableApplications == null) {
            reportableApplications =
                    applications.getApplicationNames(getReportableEnvironments(),
                            parameters.getApplications());
        }
        return reportableApplications;
    }

    public void resetReportableApplications() {

        reportableApplications = null;
    }

    public Set<String> getReportableKeys() throws TranscriptException {

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
                propertyCache = properties.getAllProperties(environment, application);
            } catch (Throwable throwable) {
            }
            if (propertyCache == null) {
                propertyCache = new TreeMap<String, String>();
            }
            environmentCache.put(application, propertyCache);
        }
        return propertyCache;
    }
}
