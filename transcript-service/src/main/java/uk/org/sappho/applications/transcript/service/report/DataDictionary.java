/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.registry.Properties;
import uk.org.sappho.applications.transcript.service.registry.TranscriptParameters;

import java.util.Map;

@Singleton
public class DataDictionary {

    private final Properties properties;
    private final TranscriptParameters transcriptParameters;
    private Map<String, Object> dictionary = null;

    @Inject
    public DataDictionary(Properties properties,
                          TranscriptParameters transcriptParameters) {

        this.properties = properties;
        this.transcriptParameters = transcriptParameters;
    }

    public Map<String, Object> getDictionary() throws TranscriptException {

        if (dictionary == null) {
            dictionary = properties.getAllProperties(
                    transcriptParameters.get("dictionary.environment", ".devops"),
                    transcriptParameters.get("dictionary.application", ".dictionary"), false);
        }
        return dictionary;
    }
}
