/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry.vcs;

import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;

import java.util.Map;

public interface VersionControlSystem {

    void update(String path) throws ConfigurationException;

    Map<String, String> getProperties(String path) throws ConfigurationException;

    void checkout() throws ConfigurationException;

    void commit(String path, boolean isNew) throws ConfigurationException;

    void delete(String path) throws ConfigurationException;
}
