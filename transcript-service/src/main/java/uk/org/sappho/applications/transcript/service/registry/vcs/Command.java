/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry.vcs;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.util.StringUtils;

public class Command {

    private CommandLine commandLine;
    private String safeCommand = "";

    public Command(String executable) {

        commandLine = new CommandLine(executable);
    }

    public void add(String part, boolean hide) {

        if (part != null && part.length() != 0) {
            commandLine.addArgument(part);
            addToSafeCommand(part, hide);
        }
    }

    public void add(String name, String value, boolean hide) {

        if (name != null && name.length() != 0 && value != null && value.length() != 0) {
            commandLine.addArgument(name);
            commandLine.addArgument(value);
            addToSafeCommand(name, false);
            addToSafeCommand(value, hide);
        }
    }

    private void addToSafeCommand(String part, boolean hide) {

        if (safeCommand.length() != 0) {
            safeCommand += " ";
        }
        if (hide) {
            safeCommand += "******";
        } else {
            safeCommand += StringUtils.quoteArgument(part);
        }
    }

    public CommandLine getCommandLine() {

        return commandLine;
    }

    public String getSafeCommand() {

        return safeCommand;
    }
}
