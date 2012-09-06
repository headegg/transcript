/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import uk.org.sappho.applications.configuration.service.vcs.VersionControlSystem;

import java.io.File;
import java.util.Map;

public class WorkingCopy {

    private String workingCopyPath;
    private String workingCopyId;
    private VersionControlSystem versionControlSystem;

    @Inject
    public WorkingCopy(@Named("working.copy.path") String workingCopyPath,
                       @Named("working.copy.id") String workingCopyId,
                       VersionControlSystem versionControlSystem) throws ConfigurationException {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.versionControlSystem = versionControlSystem;
    }

    public File getFile(String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        File workingCopy = new File(workingCopyPath, workingCopyId);
        if (workingCopy.exists()) {
            if (!workingCopy.isDirectory()) {
                throw new ConfigurationException("Requested working copy " + workingCopyId +
                        " is not a directory");
            }
        } else {
            versionControlSystem.checkout();
        }
        File svnDirectory = new File(workingCopy, ".svn");
        if (svnDirectory.exists() && svnDirectory.isDirectory()) {
            versionControlSystem.update(filename, workingCopyProperties);
        }
        File file = new File(workingCopy, filename);
        if (!file.exists()) {
            throw new ConfigurationException("Requested object " + filename + " does not exist");
        }
        return file;
    }
}
