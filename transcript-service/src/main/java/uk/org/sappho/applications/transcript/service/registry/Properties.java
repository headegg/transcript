/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

import com.google.inject.Inject;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.util.SortedMap;
import java.util.TreeMap;

public class Properties {

    private TranscriptParameters transcriptParameters;
    private WorkingCopy workingCopy;

    @Inject
    public Properties(TranscriptParameters transcriptParameters, WorkingCopy workingCopy) throws TranscriptException {

        this.transcriptParameters = transcriptParameters;
        this.workingCopy = workingCopy;
    }

    public SortedMap<String, Object> getAllProperties(String environment, String application)
            throws TranscriptException {

        return workingCopy.getProperties(environment, application);
    }

    public SortedMap<String, Object> getAllProperties(String environment, String application,
                                                      boolean includeVersionControlProperties)
            throws TranscriptException {

        return workingCopy.getProperties(environment, application, includeVersionControlProperties);
    }

    public String get(String environment, String application, String key) throws TranscriptException {

        String value = (String) getAllProperties(environment, application).get(key);
        if (value == null) {
            value = transcriptParameters.getDefaultValue();
            if (value == null) {
                throw new TranscriptException("There is no value for " + key);
            }
        }
        return value;
    }

    public void put(String environment, String application, SortedMap<String, Object> properties)
            throws TranscriptException {

        workingCopy.putProperties(environment, application, properties, transcriptParameters.isMerge());
    }

    public void put(String environment, String application, String key, String value)
            throws TranscriptException {

        if (value != null) {
            SortedMap<String, Object> properties = new TreeMap<String, Object>();
            properties.put(key, value);
            workingCopy.putProperties(environment, application, properties, true);
        } else {
            delete(environment, application, key);
        }
    }

    public void delete(String environment, String application, String key) throws TranscriptException {

        workingCopy.deleteProperty(environment, application, key);
    }

    public void delete(String environment, String application) throws TranscriptException {

        workingCopy.deleteProperties(environment, application);
    }
}
