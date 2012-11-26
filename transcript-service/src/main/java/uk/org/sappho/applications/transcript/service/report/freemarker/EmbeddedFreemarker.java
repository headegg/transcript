/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.freemarker;

import com.google.inject.Inject;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.io.StringWriter;

public class EmbeddedFreemarker {

    private final Configuration freemarkerConfiguration;

    @Inject
    public EmbeddedFreemarker(Configuration freemarkerConfiguration) {

        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    public String generate(Object data, String template)
            throws TranscriptException {

        try {
            freemarkerConfiguration.setTemplateLoader(new StringTemplateLoader(template));
            freemarkerConfiguration.setObjectWrapper(new DefaultObjectWrapper());
            Template freemarkerTemplate = freemarkerConfiguration.getTemplate("");
            StringWriter stringWriter = new StringWriter();
            freemarkerTemplate.process(data, stringWriter);
            stringWriter.close();
            return stringWriter.toString();
        } catch (Throwable throwable) {
            throw new TranscriptException("Unable to generate embedded FreeMarker content", throwable);
        }
    }
}
