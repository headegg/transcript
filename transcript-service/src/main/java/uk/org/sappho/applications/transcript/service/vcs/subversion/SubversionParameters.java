/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.vcs.subversion;

import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.vcs.VersionControlSystemParameters;

public class SubversionParameters implements VersionControlSystemParameters {

    private String url;
    private String username;
    private String password;
    private String commitMessage;
    private String executable;
    private boolean trustServerCertificate;

    public SubversionParameters(String url,
                                String username,
                                String password,
                                String commitMessage,
                                String executable,
                                boolean trustServerCertificate) {

        this.url = url;
        this.username = username;
        this.password = password;
        this.commitMessage = commitMessage;
        this.executable = executable;
        this.trustServerCertificate = trustServerCertificate;
    }

    public String getUrl() throws TranscriptException {

        if (url == null || url.length() == 0) {
            throw new TranscriptException("Subversion repository checkout URL not supplied");
        }
        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getCommitMessage() {

        return commitMessage != null && commitMessage.length() != 0 ? commitMessage : "[transcript update]";
    }

    public void setCommitMessage(String commitMessage) {

        this.commitMessage = commitMessage;
    }

    public String getExecutable() {

        return executable != null && executable.length() != 0 ? executable : "svn";
    }

    public void setExecutable(String executable) {

        this.executable = executable;
    }

    public boolean isTrustServerCertificate() {

        return trustServerCertificate;
    }

    public void setTrustServerCertificate(boolean trustServerCertificate) {

        this.trustServerCertificate = trustServerCertificate;
    }
}
