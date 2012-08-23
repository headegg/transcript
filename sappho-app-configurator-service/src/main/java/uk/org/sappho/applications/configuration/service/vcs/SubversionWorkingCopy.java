package uk.org.sappho.applications.configuration.service.vcs;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.WorkingCopyContext;

import java.io.File;

public class SubversionWorkingCopy {

    private final SVNUpdateClient svnUpdateClient = SVNClientManager.newInstance().getUpdateClient();
    private final SVNStatusClient svnStatusClient = SVNClientManager.newInstance().getStatusClient();

    public void update(String workingCopyPath, WorkingCopyContext workingCopyContext) throws ConfigurationException {

        try {
            File directory = new File(workingCopyPath);
            svnUpdateClient.doUpdate(new File[]{directory}, SVNRevision.HEAD, SVNDepth.INFINITY, true, true);
            SVNStatus svnStatus = svnStatusClient.doStatus(directory, false);
            workingCopyContext.setHeadRevision(svnStatus.getRevision().toString());
            workingCopyContext.setRepository(svnStatus.getURL().toString());
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update from Subversion server", exception);
        }
    }
}
