/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs.product;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.codehaus.plexus.util.FileUtils;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.vcs.VersionControlSystem;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class NoVersionControl implements VersionControlSystem {

    private String workingCopyPath;
    private String workingCopyId;

    @Inject
    public NoVersionControl(@Named("working.copy.path") String workingCopyPath,
                            @Named("working.copy.id") String workingCopyId) {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
    }

    public void update(String filename) {}

    public Map<String, String> getProperties(String filename) {

        return new LinkedHashMap<String, String>();
    }

    public void checkout() throws ConfigurationException {

        if (!new File(workingCopyPath, workingCopyId).mkdirs()) {
            throw new ConfigurationException("Unable to create working copy " + workingCopyId);
        }
    }

    public void commit(String filename) {}

    public void delete(String filename) throws ConfigurationException {

        try {
            FileUtils.forceDelete(new File(new File(workingCopyPath, workingCopyId), filename));
        }
        catch (Throwable throwable) {
            throw new ConfigurationException("Unable to delete " + filename);
        }
    }
}
