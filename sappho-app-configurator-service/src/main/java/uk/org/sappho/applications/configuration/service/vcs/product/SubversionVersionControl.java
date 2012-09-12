/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs.product;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.codehaus.plexus.util.FileUtils;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.vcs.Command;
import uk.org.sappho.applications.configuration.service.vcs.CommandExecuter;
import uk.org.sappho.applications.configuration.service.vcs.VersionControlSystem;

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
        patterns.put(Pattern.compile(".*<checksum>(.+)</checksum>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.hash");
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

    public void update(String filename) throws ConfigurationException {

        execute("update", new String[]{"--quiet", "--force", "--accept", "theirs-full", "."},
                new File(workingCopyPath, workingCopyId));
    }

    public Map<String, String> getProperties(String filename) {

        Map<String, String> properties = new LinkedHashMap<String, String>();
        try {
            String output = execute("info", new String[]{"--xml", filename}, new File(workingCopyPath, workingCopyId));
            for (Pattern pattern : patterns.keySet()) {
                Matcher matcher = pattern.matcher(output);
                if (matcher.matches()) {
                    properties.put(patterns.get(pattern), matcher.group(1));
                }
            }
        }
        catch (Throwable throwable) {}
        return properties;
    }

    public void checkout() throws ConfigurationException {

        if (url.length() == 0) {
            throw new ConfigurationException("Subversion repository checkout URL not supplied");
        }
        execute("checkout", new String[]{"--quiet", url, workingCopyId}, new File(workingCopyPath));
    }

    public void commit(String filename) throws ConfigurationException {

        if (commitMessage.length() == 0) {
            throw new ConfigurationException("Subversion commit message not supplied");
        }
        File basePath = new File(workingCopyPath, workingCopyId);
        String fileUrl = getFileUrl(filename);
        if (fileUrl == null) {
            try {
                execute("add", new String[]{"--force", "--quiet", "--no-ignore", filename},
                        new File(workingCopyPath, workingCopyId));
            }
            catch (ConfigurationException exception) {
                filename = new File(filename).getParent();
                if (filename != null && filename.length() != 0) {
                    execute("add", new String[]{"--force", "--quiet", "--no-ignore", filename},
                            new File(workingCopyPath, workingCopyId));
                } else {
                    throw exception;
                }
            }
        }
        execute("commit", new String[]{"--quiet", "--message", commitMessage, filename},
            new File(workingCopyPath, workingCopyId));
    }

    public void delete(String filename) throws ConfigurationException {

        File basePath = new File(workingCopyPath, workingCopyId);
        String fileUrl = getFileUrl(filename);
        if (fileUrl != null) {
            execute("delete", new String[] {"--force", "--quiet", filename}, basePath);
            commit(filename);
        } else {
            try {
                FileUtils.forceDelete(new File(basePath, filename));
            }
            catch (Throwable throwable) {
                throw new ConfigurationException("Unable to delete " + filename);
            }
        }
    }

    private String getFileUrl(String filename) {

        return getProperties(filename).get("vcs.repository.location");
    }

    private String execute(String subversionCommand, String[] parameters, File directory)
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
        return commandExecuter.execute(command, directory);
    }
}
