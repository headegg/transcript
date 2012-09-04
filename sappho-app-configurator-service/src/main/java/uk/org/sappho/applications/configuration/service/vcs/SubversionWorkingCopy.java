/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import com.google.inject.Inject;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.ServiceProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubversionWorkingCopy {

    private final static Map<Pattern, String> patterns = new HashMap<Pattern, String>();

    static {
        patterns.put(Pattern.compile("^Revision: ([0-9]*)$"), "vcs.revision");
        patterns.put(Pattern.compile("^Last Changed Rev: ([0-9]*)$"), "vcs.last.changed.revision");
        patterns.put(Pattern.compile("^Last Changed Date: (.+)$"), "vcs.last.changed.date");
        patterns.put(Pattern.compile("^Last Changed Author: (.+)$"), "vcs.last.changed.author");
        patterns.put(Pattern.compile("^URL: (.+)$"), "vcs.repository.location");
    }

    private String url;
    private String username = System.getProperty("svn.username");
    private String password = System.getProperty("svn.password");

    @Inject
    public SubversionWorkingCopy(ServiceProperties serviceProperties) {

        url = serviceProperties.getProperties().get("svn.url");
    }

    public void update(File workingCopy, String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        try {
            String command = "svn update --non-interactive --quiet --accept theirs-full " + filename;
            Process process = Runtime.getRuntime().exec(command, null, workingCopy);
            if (process.waitFor() != 0) {
                throw new ConfigurationException("Unable to update from Subversion server");
            }
            if (workingCopyProperties != null) {
                command = "svn info --non-interactive " + filename;
                process = Runtime.getRuntime().exec(command, null, workingCopy);
                if (process.waitFor() != 0) {
                    throw new ConfigurationException("Unable to get status from Subversion server");
                }
                BufferedReader info = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = info.readLine()) != null) {
                    for (Pattern pattern : patterns.keySet()) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.matches()) {
                            workingCopyProperties.put(patterns.get(pattern), matcher.group(1));
                            break;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update from Subversion server", exception);
        }
    }

    public void checkout(File workingCopyBase, String workingCopyId) throws ConfigurationException {

        if (url == null || url.length() == 0) {
            throw new ConfigurationException("Subversion repository checkout URL not specified");
        }
        String authentication =
                username != null && username.length() != 0 && password != null && password.length() != 0 ?
                        " --username " + username + " --password " + password : "";
        try {
            String command = "svn checkout --non-interactive --quiet" + authentication + " " + url + " " + workingCopyId;
            Process process = Runtime.getRuntime().exec(command, null, workingCopyBase);
            if (process.waitFor() != 0) {
                throw new ConfigurationException("Subversion checkout reported an error");
            }
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to checkout from Subversion server", exception);
        }
    }
}
