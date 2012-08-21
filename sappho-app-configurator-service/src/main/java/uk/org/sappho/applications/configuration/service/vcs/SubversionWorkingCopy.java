package uk.org.sappho.applications.configuration.service.vcs;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import uk.org.sappho.applications.configuration.service.ConfigurationException;

import java.io.File;

public class SubversionWorkingCopy {

    private final SVNUpdateClient svnUpdateClient = SVNClientManager.newInstance().getUpdateClient();
    private static Boolean updating = false;

    public void update(String workingCopyPath, String name) throws ConfigurationException {

        try {
            boolean update = false;
            synchronized (updating) {
                if (!updating) {
                    updating = true;
                    update = true;
                }
            }
            if (update) {
                svnUpdateClient.doUpdate(new File[]{new File(workingCopyPath, name)},
                        SVNRevision.HEAD, SVNDepth.INFINITY, false, false, false);
                synchronized (updating) {
                    updating = false;
                }
            }
        } catch (Exception exception) {
            throw new ConfigurationException("Unable to update " + name + " from Subversion server", exception);
        }
    }
}
