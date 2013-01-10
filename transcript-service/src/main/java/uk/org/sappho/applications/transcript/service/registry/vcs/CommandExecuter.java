/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry.vcs;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandExecuter {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String execute(Command command, File directory) throws TranscriptException {

        try {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(command.getSafeCommand());
            }
            DefaultExecutor executor = new DefaultExecutor();
            DefaultExecuteResultHandler commandResultsHandler = new DefaultExecuteResultHandler();
            ByteArrayOutputStream commandOutputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream commandErrorStream = new ByteArrayOutputStream();
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(commandOutputStream, commandErrorStream);
            executor.setWatchdog(new ExecuteWatchdog(60000));
            executor.setStreamHandler(pumpStreamHandler);
            executor.setWorkingDirectory(directory);
            executor.execute(command.getCommandLine(), commandResultsHandler);
            commandResultsHandler.waitFor();
            if (commandResultsHandler.getExitValue() != 0) {
                throw new TranscriptException(commandErrorStream.toString());
            }
            return commandOutputStream.toString();
        } catch (Throwable throwable) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(throwable.getMessage());
            }
            throw new TranscriptException("Unable to execute system command: " + command.getSafeCommand(), throwable);
        }
    }
}
