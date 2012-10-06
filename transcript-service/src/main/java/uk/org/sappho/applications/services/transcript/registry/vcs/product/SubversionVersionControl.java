/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry.vcs.product;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.vcs.Command;
import uk.org.sappho.applications.services.transcript.registry.vcs.CommandExecuter;
import uk.org.sappho.applications.services.transcript.registry.vcs.VersionControlSystem;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubversionVersionControl implements VersionControlSystem {

    private final static Map<Pattern, String> patterns = new LinkedHashMap<Pattern, String>();
    private String workingCopyPath;
    private String workingCopyId;
    private String url;
    private String username;
    private String password;
    private String commitMessage;
    private String executable;
    private String certificateTrust;
    private CommandExecuter commandExecuter;
    private String lastUpdatePath = null;

    static {
        patterns.put(Pattern.compile(".*<commit.*?revision=\"([0-9]*)\".*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.revision");
        patterns.put(Pattern.compile(".*<commit.*?<date>(.+)</date>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.date");
        patterns.put(Pattern.compile(".*<commit.*?<author>(.+)</author>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.author");
        patterns.put(Pattern.compile(".*<checksum>(.+)</checksum>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.hash");
        patterns.put(Pattern.compile(".*<url>(.+)</url>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.repository.location");
        patterns.put(Pattern.compile(".*<entry.*?revision=\"([0-9]*)\".*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.revision");
    }

    @Inject
    public SubversionVersionControl(@Named("working.copy.path") String workingCopyPath,
                                    @Named("working.copy.id") String workingCopyId,
                                    @Named("url") String url,
                                    @Named("username") String username,
                                    @Named("password") String password,
                                    @Named("commit.message") String commitMessage,
                                    @Named("svn") String executable,
                                    @Named("trust.server.certificate") String trustServerCertificate,
                                    CommandExecuter commandExecuter) {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.url = url;
        this.username = username;
        this.password = password;
        this.commitMessage = commitMessage;
        this.executable = executable.length() != 0 ? executable : "svn";
        certificateTrust = trustServerCertificate.equalsIgnoreCase("true") ? "--trust-server-cert" : "";
        this.commandExecuter = commandExecuter;
    }

    public void update(String path) throws ConfigurationException {

        if (lastUpdatePath == null || !path.startsWith(lastUpdatePath)) {
            try {
                execute("revert", new String[]{"--quiet", path.length() == 0 ? "." : path});
                execute("info", new String[]{path});
                execute("update", new String[]{"--quiet", "--force", "--accept", "theirs-full", path});
                lastUpdatePath = path;
            } catch (ConfigurationException exception) {
                if (path.length() == 0) {
                    throw exception;
                } else {
                    String parent = new File(path).getParent();
                    update(parent != null ? parent : "");
                }
            }
        }
    }

    public Map<String, String> getProperties(String path) throws ConfigurationException {

        Map<String, String> properties = new LinkedHashMap<String, String>();
        String output = execute("info", new String[]{"--xml", path});
        for (Pattern pattern : patterns.keySet()) {
            Matcher matcher = pattern.matcher(output);
            if (matcher.matches()) {
                properties.put(patterns.get(pattern), matcher.group(1));
            }
        }
        return properties;
    }

    public void checkout() throws ConfigurationException {

        if (url.length() == 0) {
            throw new ConfigurationException("Subversion repository checkout URL not supplied");
        }
        execute("checkout", new String[]{"--quiet", url, workingCopyId});
    }

    public void commit(String path, boolean isNew) throws ConfigurationException {

        checkCommitMessage();
        if (isNew) {
            execute("add", new String[]{"--quiet", "--no-ignore", path});
        }
        execute("commit", new String[]{"--quiet", "--message", commitMessage, path});
    }

    public void delete(String path) throws ConfigurationException {

        checkCommitMessage();
        execute("delete", new String[]{"--quiet", path});
        execute("commit", new String[]{"--quiet", "--message", commitMessage, path});
    }

    private void checkCommitMessage() throws ConfigurationException {

        if (commitMessage.length() == 0) {
            throw new ConfigurationException("Subversion commit message not supplied");
        }
    }

    private String execute(String subversionCommand, String[] parameters)
            throws ConfigurationException {

        Command command = new Command();
        command.add(executable, false);
        command.add(subversionCommand, false);
        command.add("--non-interactive", false);
        command.add(certificateTrust, false);
        command.add("--username", username, false);
        command.add("--password", password, true);
        for (String parameter : parameters) {
            command.add(parameter, false);
        }
        File directory = new File(workingCopyPath);
        if (!subversionCommand.equals("checkout")) {
            directory = new File(directory, workingCopyId);
        }
        return commandExecuter.execute(command, directory);
    }
}
