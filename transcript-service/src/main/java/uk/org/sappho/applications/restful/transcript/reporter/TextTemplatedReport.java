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
    private ReportDataSource reportDataSource;
    private PropertyTemplateLoader propertyTemplateLoader;

    @Inject
    public TextTemplatedReport(WorkingCopy workingCopy,
                               ReportDataSource reportDataSource,
                               PropertyTemplateLoader propertyTemplateLoader) {

        this.workingCopy = workingCopy;
        this.reportDataSource = reportDataSource;
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
            reportDataSource.loadDictionary();
            reportDataSource.setReportId(reportId);
            reportDataSource.setRequiredEnvironments(requiredEnvironments);
            reportDataSource.setRequiredApplications(requiredApplications);
            reportDataSource.setRequiredKeys(requiredKeys);
            reportDataSource.setIncludeVersionControlProperties(includeVersionControlProperties);
            reportDataSource.setIncludeUndefinedEnvironments(includeUndefinedEnvironments);
            if (propertyTemplateLoader.loadTemplate(templateName)) {
                templateLoader = propertyTemplateLoader;
            }
            Configuration freemarkerConfiguration = new Configuration();
            freemarkerConfiguration.setTemplateLoader(templateLoader);
            freemarkerConfiguration.setObjectWrapper(new DefaultObjectWrapper());
            Template template = freemarkerConfiguration.getTemplate(templateName + ".ftl");
            StringWriter stringWriter = new StringWriter();
            template.process(reportDataSource, stringWriter);
            stringWriter.close();
            return stringWriter.toString();
        } catch (Throwable throwable) {
            throw new ConfigurationException("Unable to generate HTML report", throwable);
        }
    }
}
