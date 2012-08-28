/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import uk.org.sappho.applications.configuration.service.vcs.SubversionWorkingCopy;

import java.io.File;

public class WorkingCopy {

    private static final WorkingCopy instance = new WorkingCopy();
    private final SubversionWorkingCopy subversionWorkingCopy = new SubversionWorkingCopy();
    private final String workingCopyPath = System.getProperty("working.copy.path");

    synchronized public File getFile(String filename, WorkingCopyContext workingCopyContext) throws ConfigurationException {

        if (workingCopyPath == null) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        File svnDirectory = new File(workingCopyPath, ".svn");
        if (svnDirectory.exists() && svnDirectory.isDirectory()) {
            subversionWorkingCopy.update(workingCopyPath, filename, workingCopyContext);
        }
        File file = new File(workingCopyPath, filename);
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
