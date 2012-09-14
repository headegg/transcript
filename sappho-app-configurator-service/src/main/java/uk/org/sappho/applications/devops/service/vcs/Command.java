/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.devops.service.vcs;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private List<String> command = new ArrayList<String>();
    private String safeCommand = "";

    public void add(String part, boolean hide) {

        if (part != null && part.length() != 0) {
            command.add(part);
            addToSafeCommand(part, hide);
        }
    }

    public void add(String name, String value, boolean hide) {

        if (name != null && name.length() != 0 && value != null && value.length() != 0) {
            command.add(name);
            command.add(value);
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
            if (part.contains(" ")) {
                safeCommand += "\"" + part + "\"";
            } else {
                safeCommand += part;
            }
        }
    }

    public List<String> getCommand() {

        return command;
    }

    public String getSafeCommand() {

        return safeCommand;
    }
}
