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

    private ServiceProperties serviceProperties;
    private SubversionWorkingCopy subversionWorkingCopy;

    @Inject
    public WorkingCopy(ServiceProperties serviceProperties, SubversionWorkingCopy subversionWorkingCopy) throws ConfigurationException {

        this.serviceProperties = serviceProperties;
        this.subversionWorkingCopy = subversionWorkingCopy;
    }

    public File getFile(String filename, Map<String, String> workingCopyProperties) throws ConfigurationException {

        File workingCopy = new File(serviceProperties.getWorkingCopyBase(), serviceProperties.getWorkingCopyId());
        if (workingCopy.exists()) {
            if (!workingCopy.isDirectory()) {
                throw new ConfigurationException("Requested working copy " + serviceProperties.getWorkingCopyId() +
                        " is not a directory");
            }
        } else {
            String vcs = serviceProperties.getProperties().get("vcs");
            if (vcs == null || vcs.length() == 0) {
                throw new ConfigurationException("No VCS specified for configuration repository cloning");
            }
            if (vcs.equalsIgnoreCase("svn")) {
                subversionWorkingCopy.checkout();
            } else {
                throw new ConfigurationException("VCS " + vcs + " isn't a known system");
            }
        }
        File svnDirectory = new File(workingCopy, ".svn");
        if (svnDirectory.exists() && svnDirectory.isDirectory()) {
            subversionWorkingCopy.update(filename, workingCopyProperties);
        }
        File file = new File(workingCopy, filename);
        if (!file.exists()) {
            throw new ConfigurationException("Requested object " + filename + " does not exist");
        }
        return file;
    }
}
