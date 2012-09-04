/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.Inject;
import uk.org.sappho.applications.configuration.service.vcs.SubversionWorkingCopy;

import java.io.File;
import java.util.Map;

public class WorkingCopy {

    private SubversionWorkingCopy subversionWorkingCopy;
    private File workingCopyBase;
    private String workingCopyId;
    private String vcs;

    @Inject
    public WorkingCopy(ServiceProperties serviceProperties, SubversionWorkingCopy subversionWorkingCopy) throws ConfigurationException {

        String workingCopyPath = System.getProperty("working.copy.path");
        if (workingCopyPath == null || workingCopyPath.length() == 0) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        workingCopyBase = new File(workingCopyPath);
        workingCopyId = serviceProperties.getProperties().get("workingCopyId");
        if (workingCopyId == null || workingCopyId.length() == 0) {
            workingCopyId = "default";
        }
        vcs = serviceProperties.getProperties().get("vcs");
        this.subversionWorkingCopy = subversionWorkingCopy;
    }

    public File getFile(String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        File workingCopy = new File(workingCopyBase, workingCopyId);
        if (workingCopy.exists()) {
            if (!workingCopy.isDirectory()) {
                throw new ConfigurationException("Requested working copy " + workingCopyId + " is not a directory");
            }
        } else {
            if (vcs == null || vcs.length() == 0) {
                throw new ConfigurationException("No VCS specified for configuration repository cloning");
            }
            if (vcs.equalsIgnoreCase("svn")) {
                subversionWorkingCopy.checkout(workingCopyBase, workingCopyId);
            } else {
                throw new ConfigurationException("VCS " + vcs + " isn't a known system");
            }
        }
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
}
