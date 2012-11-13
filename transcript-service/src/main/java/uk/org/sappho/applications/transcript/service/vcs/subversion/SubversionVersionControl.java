/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.vcs.subversion;

import com.google.inject.Inject;
import uk.org.sappho.applications.transcript.service.registry.ConfigurationException;
import uk.org.sappho.applications.transcript.service.registry.TranscriptParameters;
import uk.org.sappho.applications.transcript.service.vcs.Command;
import uk.org.sappho.applications.transcript.service.vcs.CommandExecuter;
import uk.org.sappho.applications.transcript.service.vcs.VersionControlSystem;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubversionVersionControl implements VersionControlSystem {

    private TranscriptParameters transcriptParameters;
    private SubversionParameters subversionParameters;
    private CommandExecuter commandExecuter;

    private String lastUpdatePath = null;

    private final static Map<Pattern, String> PATTERNS = new LinkedHashMap<Pattern, String>();

    static {
        PATTERNS.put(Pattern.compile(".*<commit.*?revision=\"([0-9]*)\".*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.revision");
        PATTERNS.put(Pattern.compile(".*<commit.*?<date>(.+)</date>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.date");
        PATTERNS.put(Pattern.compile(".*<commit.*?<author>(.+)</author>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.last.changed.author");
        PATTERNS.put(Pattern.compile(".*<checksum>(.+)</checksum>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.hash");
        PATTERNS.put(Pattern.compile(".*<url>(.+)</url>.*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.repository.location");
        PATTERNS.put(Pattern.compile(".*<entry.*?revision=\"([0-9]*)\".*", Pattern.MULTILINE + Pattern.DOTALL),
                "vcs.revision");
    }

    @Inject
    public SubversionVersionControl(TranscriptParameters transcriptParameters,
                                    SubversionParameters subversionParameters,
                                    CommandExecuter commandExecuter) {

        this.transcriptParameters = transcriptParameters;
        this.subversionParameters = subversionParameters;
        this.commandExecuter = commandExecuter;
    }

    public void update(String path) {

        if (lastUpdatePath == null || !path.startsWith(lastUpdatePath)) {
            try {
                if (!transcriptParameters.isReadOnly()) {
                    execute("revert", new String[]{"--quiet", path.length() == 0 ? "." : path});
                }
                execute("info", new String[]{path});
                execute("update", new String[]{"--quiet", "--force", "--accept", "theirs-full", path});
                lastUpdatePath = path;
            } catch (ConfigurationException exception) {
                if (path.length() != 0) {
                    String parent = new File(path).getParent();
                    update(parent != null ? parent : "");
                }
            }
        }
    }

    public Map<String, String> getProperties(String path) throws ConfigurationException {

        Map<String, String> properties = new LinkedHashMap<String, String>();
        String output = execute("info", new String[]{"--xml", path});
        for (Pattern pattern : PATTERNS.keySet()) {
            Matcher matcher = pattern.matcher(output);
            if (matcher.matches()) {
                properties.put(PATTERNS.get(pattern), matcher.group(1));
            }
        }
        return properties;
    }

    public void checkout() throws ConfigurationException {

        execute("checkout", new String[]{"--quiet", subversionParameters.getUrl(),
                transcriptParameters.getWorkingCopyId()});
    }

    public void commit(String path, boolean isNew) throws ConfigurationException {

        if (isNew) {
            execute("add", new String[]{"--quiet", "--no-ignore", path});
        }
        execute("commit", new String[]{"--quiet", "--message", subversionParameters.getCommitMessage(), path});
    }

    public void delete(String path) throws ConfigurationException {

        execute("delete", new String[]{"--quiet", path});
        execute("commit", new String[]{"--quiet", "--message", subversionParameters.getCommitMessage(), path});
    }

    private String execute(String subversionCommand, String[] parameters)
            throws ConfigurationException {

        Command command = new Command();
        command.add(subversionParameters.getExecutable(), false);
        command.add(subversionCommand, false);
        command.add("--non-interactive", false);
        command.add(subversionParameters.isTrustServerCertificate() ? "--trust-server-cert" : "", false);
        command.add("--username", subversionParameters.getUsername(), false);
        command.add("--password", subversionParameters.getPassword(), true);
        for (String parameter : parameters) {
            command.add(parameter, false);
        }
        File directory = new File(transcriptParameters.getWorkingCopyPath());
        if (!subversionCommand.equals("checkout")) {
            directory = new File(directory, transcriptParameters.getWorkingCopyId());
        }
        return commandExecuter.execute(command, directory);
    }
}
