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
    private final static Pattern urlPattern = Pattern.compile("^URL: (.*)$");

    public void update(String workingCopyPath, WorkingCopyContext workingCopyContext) throws ConfigurationException {

        try {
            File directory = new File(workingCopyPath);
            Process process = Runtime.getRuntime().exec("svn update", null, directory);
            process.waitFor();
            process.destroy();
            process = Runtime.getRuntime().exec("svn info", null, directory);
            process.waitFor();
            BufferedReader info = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String revision = "";
            String url = "";
            String line;
            while ((revision.length() == 0 || url.length() == 0) && (line = info.readLine()) != null) {
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
