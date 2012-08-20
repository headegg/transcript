package uk.org.sappho.applications.configuration.service.vcs;

import org.tigris.subversion.javahl.Depth;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.SVNClient;
import uk.org.sappho.applications.configuration.service.ConfigurationException;

import java.io.File;

public class SubversionWorkingCopy {

    private static final SVNClient svnClient = new SVNClient();

    public void update(String workingCopyPath, String name) throws ConfigurationException {

        try {
            svnClient.update(new File(workingCopyPath, name).getCanonicalPath(),
                    Revision.HEAD, Depth.infinity, false, false, false);
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update " + name + " from Subversion server", exception);
        }
    }
}
