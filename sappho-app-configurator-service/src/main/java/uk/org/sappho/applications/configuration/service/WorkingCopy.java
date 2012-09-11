/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import uk.org.sappho.applications.configuration.service.vcs.VersionControlSystem;

import java.io.File;
import java.util.HashMap;
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

    public File getUpToDateFile(String filename) throws ConfigurationException {

        File workingCopy = new File(workingCopyPath, workingCopyId);
        if (workingCopy.exists()) {
            if (!workingCopy.isDirectory()) {
                throw new ConfigurationException("Requested working copy " + workingCopyId +
                        " is not a directory");
            }
            versionControlSystem.update(filename);
        } else {
            versionControlSystem.checkout();
        }
        return new File(workingCopy, filename);
    }

    public Map<String, String> getVersionControlProperties(String filename) {

        Map<String, String> properties = new HashMap<String, String>();
        try {
            versionControlSystem.getProperties(filename, properties);
        } catch (Throwable throwable) {
        }
        return properties;
    }

    public File getFile(String filename) {

        return new File(new File(workingCopyPath, workingCopyId), filename);
    }

    public void commit(String filename) throws ConfigurationException {

        versionControlSystem.commit(filename);
    }
}
