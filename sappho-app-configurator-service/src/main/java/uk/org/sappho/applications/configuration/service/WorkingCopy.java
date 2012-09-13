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
import java.util.Map;

public class WorkingCopy {

    private String workingCopyPath;
    private String workingCopyId;
    private VersionControlSystem versionControlSystem;
    private Object lock;

    @Inject
    public WorkingCopy(@Named("working.copy.path") String workingCopyPath,
                       @Named("working.copy.id") String workingCopyId,
                       VersionControlSystem versionControlSystem,
                       WorkingCopySynchronizer workingCopySynchronizer) throws ConfigurationException {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.versionControlSystem = versionControlSystem;
        lock = workingCopySynchronizer.getLock(workingCopyId);
    }

    public File getUpToDateFile(String filename) throws ConfigurationException {

        synchronized (lock) {
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
    }

    public File getFile(String filename) {

        synchronized (lock) {
            return new File(new File(workingCopyPath, workingCopyId), filename);
        }
    }

    public Map<String, String> getVersionControlProperties(String filename) {

        synchronized (lock) {
            return versionControlSystem.getProperties(filename);
        }
    }

    public void commit(String filename) throws ConfigurationException {

        synchronized (lock) {
            versionControlSystem.commit(filename);
        }
    }

    public void delete(String filename) throws ConfigurationException {

        synchronized (lock) {
            versionControlSystem.delete(filename);
        }
    }
}
