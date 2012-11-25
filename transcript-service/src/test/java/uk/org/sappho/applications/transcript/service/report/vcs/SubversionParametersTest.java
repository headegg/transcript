/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.vcs;

import junit.framework.Assert;
import org.junit.Test;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.vcs.subversion.SubversionParameters;

public class SubversionParametersTest {

    @Test
    public void testSubversionParameters() throws TranscriptException {

        SubversionParameters subversionParameters = new SubversionParameters("http://example.com/svn",
                "user", "password", "Test commit.", null, true);
        Assert.assertEquals("The Subversion URL isn't correct",
                subversionParameters.getUrl(), "http://example.com/svn");
        Assert.assertEquals("The username isn't correct",
                subversionParameters.getUsername(), "user");
        Assert.assertEquals("The password isn't correct",
                subversionParameters.getPassword(), "password");
        Assert.assertEquals("The commit message isn't correct",
                subversionParameters.getCommitMessage(), "Test commit.");
        Assert.assertEquals("The Subversion executable isn't correct",
                subversionParameters.getExecutable(), "svn");
        Assert.assertEquals("The trust flag isn't correct",
                subversionParameters.isTrustServerCertificate(), true);
    }
}
