/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs.product;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.vcs.CommandExecuter;
import uk.org.sappho.applications.configuration.service.vcs.VersionControlSystem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubversionVersionControl implements VersionControlSystem {

    private final static Map<Pattern, String> patterns = new HashMap<Pattern, String>();
    private final static String checkoutCommand = "svn checkout --non-interactive --quiet";
    private String workingCopyPath;
    private String workingCopyId;
    private String url;
    private String username;
    private String password;
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
    public SubversionVersionControl(@Named("working.copy.path") String workingCopyPath,
                                    @Named("working.copy.id") String workingCopyId,
                                    @Named("svn.url") String url,
                                    @Named("svn.username") String username,
                                    @Named("svn.password") String password,
                                    CommandExecuter commandExecuter) {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.url = url;
        this.username = username;
        this.password = password;
        this.commandExecuter = commandExecuter;
    }

    public void update(String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        try {
            File workingCopy = new File(workingCopyPath, workingCopyId);
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

        if (url.length() == 0) {
            throw new ConfigurationException("Subversion repository checkout URL not specified");
        }
        String authentication = username.length() != 0 && password.length() != 0 ?
                " --username " + username + " --password " + password : "";
        try {
            String directorySpec = " " + url + " " + workingCopyId;
            String command = checkoutCommand + authentication + directorySpec;
            commandExecuter.execute(command, new File(workingCopyPath), checkoutCommand + directorySpec);
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to checkout from Subversion server", exception);
        }
    }
}
