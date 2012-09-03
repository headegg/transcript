/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import uk.org.sappho.applications.configuration.service.ConfigurationException;

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

    public void update(File workingCopy, String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        try {
            Process process = Runtime.getRuntime().exec("svn update --non-interactive --quiet --accept theirs-full " + filename, null, workingCopy);
            if (process.waitFor() != 0) {
                throw new ConfigurationException("Unable to update from Subversion server");
            }
            process.destroy();
            if (workingCopyProperties != null) {
                process = Runtime.getRuntime().exec("svn info --non-interactive " + filename, null, workingCopy);
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
                process.destroy();
            }
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update from Subversion server", exception);
        }
    }
}
