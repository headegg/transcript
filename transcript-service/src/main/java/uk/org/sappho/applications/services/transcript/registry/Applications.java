/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry;

import com.google.inject.Inject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Applications {

    private WorkingCopy workingCopy;

    private static final Pattern ALL = Pattern.compile("^[^\\.].*\\.json$");

    @Inject
    public Applications(WorkingCopy workingCopy) {

        this.workingCopy = workingCopy;
    }

    public String[] getApplicationNames(String environment) throws ConfigurationException {

        return getApplicationNames(environment, ALL);
    }

    public String[] getApplicationNames(String environment, String applicationNamePrefix)
            throws ConfigurationException {

        return getApplicationNames(environment,
                Pattern.compile("^" + applicationNamePrefix.replace(".", "\\.") + ".*\\.json$"));
    }

    private String[] getApplicationNames(String environment, final Pattern pattern) throws ConfigurationException {

        String[] applications = workingCopy.getUpToDatePath(environment).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile() && pattern.matcher(name).matches();
            }
        });
        if (applications != null) {
            for (int i = 0; i < applications.length; i++) {
                String name = applications[i];
                applications[i] = name.substring(0, name.length() - 5);
            }
            Arrays.sort(applications);
        } else {
            applications = new String[0];
        }
        return applications;
    }

    public Set<String> getApplicationNames(Set<String> requiredEnvironments, String[] requiredApplications)
            throws ConfigurationException {

        Map<String, String> applications = new TreeMap<String, String>();
        if (requiredApplications == null || requiredApplications.length == 0) {
            for (String environment : requiredEnvironments) {
                for (String application : getApplicationNames(environment)) {
                    applications.put(application, application);
                }
            }
        } else {
            for (String environment : requiredEnvironments) {
                for (String requiredApplication : requiredApplications) {
                    if (requiredApplication.endsWith("*")) {
                        for (String discoveredApplication :
                                getApplicationNames(environment,
                                        requiredApplication.substring(0, requiredApplication.length() - 1))) {
                            applications.put(discoveredApplication, discoveredApplication);
                        }
                    } else {
                        applications.put(requiredApplication, requiredApplication);
                    }
                }
            }
        }
        return applications.keySet();
    }
}
