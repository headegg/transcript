/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.vcs.mock;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.registry.TranscriptParameters;
import uk.org.sappho.applications.transcript.service.vcs.VersionControlSystem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MockVersionControl implements VersionControlSystem {

    private TranscriptParameters transcriptParameters;

    private static final File SOURCE = new File("src/test/files/data/store");

    @Inject
    public MockVersionControl(TranscriptParameters transcriptParameters) {

        this.transcriptParameters = transcriptParameters;
    }

    public void update(String path) {
    }

    public Map<String, String> getProperties(String path) throws TranscriptException {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("vcs.last.change.author", "anon-e-mouse");
        properties.put("vcs.last.change.hash", "1234567890");
        return properties;
    }

    public void checkout() throws TranscriptException {

        try {
            File workingDirectory =
                    new File(transcriptParameters.getWorkingCopyPath(), transcriptParameters.getWorkingCopyId());
            FileUtils.copyDirectory(SOURCE, workingDirectory);
        } catch (Throwable throwable) {
            throw new TranscriptException("Unable to \"checkout\" test data", throwable);
        }
    }

    public void commit(String path, boolean isNew) throws TranscriptException {
    }

    public void delete(String path) throws TranscriptException {
    }
}
