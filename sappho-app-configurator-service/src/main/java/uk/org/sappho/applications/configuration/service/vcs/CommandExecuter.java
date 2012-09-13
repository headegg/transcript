/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import uk.org.sappho.applications.configuration.service.ConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandExecuter {

    private Object lock;

    @Inject
    public CommandExecuter(@Named("working.copy.id") String workingCopyId,
                           CommandSynchronizer commandSynchronizer) {

        lock = commandSynchronizer.getLock(workingCopyId);
    }

    public String execute(Command command, File directory) throws ConfigurationException {

        synchronized (lock) {
            try {
                String[] commandArray = new String[command.getCommand().size()];
                Process process = Runtime.getRuntime().exec(command.getCommand().toArray(commandArray), null, directory);
                int exitCode = process.waitFor();
                String standardOutput = processOutput(process.getInputStream());
                if (exitCode != 0) {
                    throw new ConfigurationException(processOutput(process.getErrorStream()));
                }
                return standardOutput;
            } catch (Throwable throwable) {
                throw new ConfigurationException("Unable to execute system command: " + command.getSafeCommand(), throwable);
            }
        }
    }

    private String processOutput(InputStream inputStream) throws IOException {

        String output = "";
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = reader.readLine()) != null) {
            output += line + "\n";
        }
        reader.close();
        return output;
    }
}
