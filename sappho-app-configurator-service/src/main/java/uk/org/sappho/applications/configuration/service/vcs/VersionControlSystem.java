/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs;

import uk.org.sappho.applications.configuration.service.ConfigurationException;

import java.util.Map;

public interface VersionControlSystem {

    void update(String filename) throws ConfigurationException;

    Map<String, String> getProperties(String filename) throws ConfigurationException;

    void checkout() throws ConfigurationException;

    void commit(String filename, boolean isNew) throws ConfigurationException;

    void delete(String filename) throws ConfigurationException;
}
