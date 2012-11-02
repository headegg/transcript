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

public class Environments {

    private WorkingCopy workingCopy;
    private Properties properties;

    private static final Pattern ALL = Pattern.compile("^[^\\.].*$");
    private static final Pattern IGNORE = Pattern.compile("^(\\.svn|\\.|\\.\\.)$");

    @Inject
    public Environments(WorkingCopy workingCopy, Properties properties) {

        this.workingCopy = workingCopy;
        this.properties = properties;
    }

    public String[] getEnvironmentNames() throws ConfigurationException {

        return getEnvironmentNames(ALL);
    }

    public String[] getEnvironmentNames(String environmentNamePrefix) throws ConfigurationException {

        return getEnvironmentNames(Pattern.compile("^" + environmentNamePrefix.replace(".", "\\.") + ".*$"));
    }

    private String[] getEnvironmentNames(final Pattern pattern) throws ConfigurationException {

        String[] environments = workingCopy.getUpToDatePath("").list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory() && pattern.matcher(name).matches() &&
                        !IGNORE.matcher(name).matches();
            }
        });
        if (environments != null) {
            Arrays.sort(environments);
        } else {
            environments = new String[0];
        }
        return environments;
    }

    public Set<String> getEnvironmentNames(String[] requiredEnvironments, String applicationName,
                                           boolean includeUndefinedEnvironments)
            throws ConfigurationException {

        Map<String, String> environments = null;
        if (requiredEnvironments == null || requiredEnvironments.length == 0) {
            requiredEnvironments = getEnvironmentNames();
            environments = new TreeMap<String, String>();
            for (String environment : requiredEnvironments) {
                if (includeUndefinedEnvironments ||
                        !properties.getAllProperties(environment, applicationName, false).isEmpty()) {
                    environments.put(environment, environment);
                }
            }
        }
        if (environments == null) {
            environments = new TreeMap<String, String>();
            for (String environment : requiredEnvironments) {
                if (environment.endsWith("*")) {
                    for (String discoveredEnvironment :
                            getEnvironmentNames(environment.substring(0, environment.length() - 1))) {
                        environments.put(discoveredEnvironment, discoveredEnvironment);
                    }
                } else {
                    environments.put(environment, environment);
                }
            }
        }
        return environments.keySet();
    }
}
