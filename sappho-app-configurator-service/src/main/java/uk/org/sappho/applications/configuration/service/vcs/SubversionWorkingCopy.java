/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.WorkingCopyContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubversionWorkingCopy {

    private final static Pattern revisionPattern = Pattern.compile("^Revision: ([0-9]*)$");
    private final static Pattern urlPattern = Pattern.compile("^URL: (.+)$");
    private final static String unknown = "unknown";

    public void update(String workingCopyPath, String filename, WorkingCopyContext workingCopyContext) throws ConfigurationException {

        try {
            File directory = new File(workingCopyPath);
            Process process = Runtime.getRuntime().exec("svn update --non-interactive --quiet --accept theirs-full " + filename, null, directory);
            process.waitFor();
            process.destroy();
            process = Runtime.getRuntime().exec("svn info --non-interactive " + filename, null, directory);
            process.waitFor();
            BufferedReader info = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String revision = unknown;
            String url = unknown;
            String line;
            while ((revision.equals(unknown) || url.equals(unknown)) && (line = info.readLine()) != null) {
                Matcher matcher = revisionPattern.matcher(line);
                if (matcher.matches()) {
                    revision = matcher.group(1);
                }
                matcher = urlPattern.matcher(line);
                if (matcher.matches()) {
                    url = matcher.group(1);
                }
            }
            process.destroy();
            workingCopyContext.setHeadRevision(revision);
            workingCopyContext.setRepository(url);
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update from Subversion server", exception);
        }
    }
}
