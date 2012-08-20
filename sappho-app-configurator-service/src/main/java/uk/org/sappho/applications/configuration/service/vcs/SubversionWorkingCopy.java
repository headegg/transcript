package uk.org.sappho.applications.configuration.service.vcs;

import org.tigris.subversion.javahl.ClientException;
import org.tigris.subversion.javahl.Depth;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.SVNClient;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.VersionedWorkingCopy;

import java.io.File;
import java.io.IOException;

public class SubversionWorkingCopy implements VersionedWorkingCopy {

    private String path;
    private static SVNClient svnClient = new SVNClient();

    public SubversionWorkingCopy(String path) {

        this.path = path;
    }

    public void update(String subpath) throws ConfigurationException {

        try {
            svnClient.update(new File(path, subpath).getCanonicalPath(), Revision.HEAD, Depth.infinity, false, false, false);
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update " + subpath, exception);
        }
    }
}
