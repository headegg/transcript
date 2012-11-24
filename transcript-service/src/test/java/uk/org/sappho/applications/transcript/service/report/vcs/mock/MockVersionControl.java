/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.vcs.mock;

import com.google.inject.Inject;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.registry.TranscriptParameters;
import uk.org.sappho.applications.transcript.service.vcs.VersionControlSystem;

import java.util.HashMap;
import java.util.Map;

public class MockVersionControl implements VersionControlSystem {

    public void update(String path) {
    }

    public Map<String, String> getProperties(String path) throws TranscriptException {

        return new HashMap<String, String>();
    }

    public void checkout() throws TranscriptException {
    }

    public void commit(String path, boolean isNew) throws TranscriptException {
    }

    public void delete(String path) throws TranscriptException {
    }
}
