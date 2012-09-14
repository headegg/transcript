/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import uk.org.sappho.applications.configuration.service.ConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class CommandExecuter {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String execute(Command command, File directory) throws ConfigurationException {

        try {
            logger.info(command.getSafeCommand());
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
