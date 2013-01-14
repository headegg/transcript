/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry.vcs;

import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.util.Map;

public interface VersionControlSystem {

    void update(String path);

    void clearUpdateCache();

    Map<String, String> getProperties(String path) throws TranscriptException;

    void checkout() throws TranscriptException;

    void commit(String path, boolean isNew) throws TranscriptException;

    void delete(String path) throws TranscriptException;
}
