/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report;

import com.google.inject.Inject;
import freemarker.cache.TemplateLoader;
import uk.org.sappho.applications.transcript.service.registry.TranscriptParameters;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class PropertyTemplateLoader implements TemplateLoader {

    private final ReportData reportData;
    private TemplateLoader sourceTemplateLoader;

    @Inject
    public PropertyTemplateLoader(ReportData reportData) {

        this.reportData = reportData;
    }

    public void setSourceTemplateLoader(TemplateLoader sourceTemplateLoader) {

        this.sourceTemplateLoader = sourceTemplateLoader;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {

        String template = null;
        if (name.endsWith(".ftl")) {
            TranscriptParameters transcriptParameters = reportData.getParameters();
            try {
                template = (String) reportData.getProperties(
                        transcriptParameters.get("templates.environment", ".devops"),
                        transcriptParameters.get("templates.application", ".templates"), false)
                        .get(name.substring(0, name.length() - 4));
            } catch (Throwable throwable) {
            }
        }
        return template != null ? template : sourceTemplateLoader.findTemplateSource(name);
    }

    @Override
    public long getLastModified(Object templateSource) {

        return templateSource instanceof String ?
                System.currentTimeMillis() : sourceTemplateLoader.getLastModified(templateSource);
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {

        return templateSource instanceof String ?
                new StringReader((String) templateSource) : sourceTemplateLoader.getReader(templateSource, encoding);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {

        if (!(templateSource instanceof String)) {
            sourceTemplateLoader.closeTemplateSource(templateSource);
        }
    }
}
