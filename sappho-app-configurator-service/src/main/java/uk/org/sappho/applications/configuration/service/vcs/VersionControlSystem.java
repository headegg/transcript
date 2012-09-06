/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import uk.org.sappho.applications.configuration.service.ConfigurationException;

import java.util.Map;

public interface VersionControlSystem {

    void update(String filename, Map<String, String> workingCopyProperties) throws ConfigurationException;

    void checkout() throws ConfigurationException;
}
