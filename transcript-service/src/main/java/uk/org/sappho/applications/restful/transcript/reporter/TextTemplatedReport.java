/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.reporter;

import com.google.inject.Inject;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.WorkingCopy;

import java.io.StringWriter;

public class TextTemplatedReport {

    private WorkingCopy workingCopy;
    private ReportData reportData;
    private PropertyTemplateLoader propertyTemplateLoader;

    @Inject
    public TextTemplatedReport(WorkingCopy workingCopy,
                               ReportData reportData,
                               PropertyTemplateLoader propertyTemplateLoader) {

        this.workingCopy = workingCopy;
        this.reportData = reportData;
        this.propertyTemplateLoader = propertyTemplateLoader;
    }

    public String generate(String templateName,
                           String reportId,
                           String[] requiredEnvironments,
                           String[] requiredApplications,
                           String[] requiredKeys,
                           boolean includeVersionControlProperties,
                           boolean includeUndefinedEnvironments,
                           TemplateLoader templateLoader)
            throws ConfigurationException {

        try {
            workingCopy.getUpToDatePath("");
            reportData.setReportId(reportId);
            reportData.setRequiredEnvironments(requiredEnvironments);
            reportData.setRequiredApplications(requiredApplications);
            reportData.setRequiredKeys(requiredKeys);
            reportData.setIncludeVersionControlProperties(includeVersionControlProperties);
            reportData.setIncludeUndefinedEnvironments(includeUndefinedEnvironments);
            if (propertyTemplateLoader.loadTemplate(templateName)) {
                templateLoader = propertyTemplateLoader;
            }
            Configuration freemarkerConfiguration = new Configuration();
            freemarkerConfiguration.setTemplateLoader(templateLoader);
            freemarkerConfiguration.setObjectWrapper(new DefaultObjectWrapper());
            Template template = freemarkerConfiguration.getTemplate(templateName + ".ftl");
            StringWriter stringWriter = new StringWriter();
            template.process(reportData, stringWriter);
            stringWriter.close();
            return stringWriter.toString();
        } catch (Throwable throwable) {
            throw new ConfigurationException("Unable to generate HTML report", throwable);
        }
    }
}
