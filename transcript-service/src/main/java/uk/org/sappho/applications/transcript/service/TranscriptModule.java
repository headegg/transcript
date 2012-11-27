/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service;

import com.google.inject.AbstractModule;

public class TranscriptModule extends AbstractModule {

    private final TranscriptParameters transcriptParameters;

    public TranscriptModule(TranscriptParameters transcriptParameters) throws TranscriptException {

        this.transcriptParameters = transcriptParameters;
    }

    @Override
    protected void configure() {

        bind(TranscriptParameters.class).toInstance(transcriptParameters);
    }
}
