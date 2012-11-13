/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.vcs;

public interface VersionControlSystemParameters {

    public String getCommitMessage();

    public void setCommitMessage(String commitMessage);
}
