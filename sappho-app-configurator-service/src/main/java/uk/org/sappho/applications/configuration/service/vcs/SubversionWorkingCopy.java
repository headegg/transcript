/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import com.google.inject.Inject;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.ServiceProperties;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubversionWorkingCopy {

    private final static Map<Pattern, String> patterns = new HashMap<Pattern, String>();
    private final static String checkoutCommand = "svn checkout --non-interactive --quiet";
    private String username = System.getProperty("svn.username");
    private String password = System.getProperty("svn.password");
    private ServiceProperties serviceProperties;
    private CommandExecuter commandExecuter;

    static {
        patterns.put(Pattern.compile(".*<entry.*?revision=\"([0-9]*)\".*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.revision");
        patterns.put(Pattern.compile(".*<commit.*?revision=\"([0-9]*)\".*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.revision");
        patterns.put(Pattern.compile(".*<commit.*?<date>(.+)</date>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.date");
        patterns.put(Pattern.compile(".*<commit.*?<author>(.+)</author>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.author");
        patterns.put(Pattern.compile(".*<url>(.+)</url>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.repository.location");
    }

    @Inject
    public SubversionWorkingCopy(ServiceProperties serviceProperties, CommandExecuter commandExecuter) {

        this.serviceProperties = serviceProperties;
        this.commandExecuter = commandExecuter;
    }

    public void update(String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        try {
            File workingCopy = new File(serviceProperties.getWorkingCopyBase(), serviceProperties.getWorkingCopyId());
            String command = "svn update --non-interactive --quiet --accept theirs-full " + filename;
            commandExecuter.execute(command, workingCopy, command);
            if (workingCopyProperties != null) {
                command = "svn info --non-interactive --xml " + filename;
                String output = commandExecuter.execute(command, workingCopy, command);
                for (Pattern pattern : patterns.keySet()) {
                    Matcher matcher = pattern.matcher(output);
                    if (matcher.matches()) {
                        workingCopyProperties.put(patterns.get(pattern), matcher.group(1));
                    }
                }
            }
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update from Subversion server", exception);
        }
    }

    public void checkout() throws ConfigurationException {

        String url = serviceProperties.getProperties().get("svn.url");
        if (url == null || url.length() == 0) {
            throw new ConfigurationException("Subversion repository checkout URL not specified");
        }
        String authentication =
                username != null && username.length() != 0 && password != null && password.length() != 0 ?
                        " --username " + username + " --password " + password : "";
        try {
            String directorySpec = " " + url + " " + serviceProperties.getWorkingCopyId();
            String command = checkoutCommand + authentication + directorySpec;
            commandExecuter.execute(command, serviceProperties.getWorkingCopyBase(), checkoutCommand + directorySpec);
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to checkout from Subversion server", exception);
        }
    }
}
