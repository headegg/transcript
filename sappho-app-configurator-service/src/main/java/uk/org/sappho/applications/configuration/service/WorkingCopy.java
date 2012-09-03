/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import uk.org.sappho.applications.configuration.service.vcs.SubversionWorkingCopy;

import java.io.File;
import java.util.Map;

public class WorkingCopy {

    private static final WorkingCopy instance = new WorkingCopy();
    private final SubversionWorkingCopy subversionWorkingCopy = new SubversionWorkingCopy();
    private final String workingCopyPath = System.getProperty("working.copy.path");

    synchronized public File getFile(String workingCopyId, String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        if (workingCopyPath == null) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        File workingCopy = new File(workingCopyPath, workingCopyId != null ? workingCopyId : ".");
        File svnDirectory = new File(workingCopy, ".svn");
        if (svnDirectory.exists() && svnDirectory.isDirectory()) {
            subversionWorkingCopy.update(workingCopy, filename, workingCopyProperties);
        }
        File file = new File(workingCopy, filename);
        if (!file.exists()) {
            throw new ConfigurationException("Requested object " + filename + " does not exist");
        }
        return file;
    }

    public static WorkingCopy getInstance() {

        return instance;
    }

    private WorkingCopy() {
    }
}
