/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.jersey;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.registry.TranscriptModule;
import uk.org.sappho.applications.transcript.service.registry.TranscriptParameters;
import uk.org.sappho.applications.transcript.service.vcs.subversion.SubversionModule;
import uk.org.sappho.applications.transcript.service.vcs.subversion.SubversionParameters;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RestServiceContext<T> {

    private final UriInfo uriInfo;
    private final ServletContext servletContext;
    private final Class<T> type;

    public RestServiceContext(UriInfo uriInfo, ServletContext servletContext, Class<T> type) {

        this.uriInfo = uriInfo;
        this.servletContext = servletContext;
        this.type = type;
    }

    public T getService() throws TranscriptException {

        Map<String, String> parameters = new TreeMap<String, String>();
        if (System.getProperty("use.system.properties", "false").equalsIgnoreCase("true")) {
            for (String key : System.getProperties().stringPropertyNames()) {
                parameters.put(key, System.getProperty(key));
            }
        }
        Enumeration parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement().toString();
            parameters.put(key, servletContext.getInitParameter(key));
        }
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        for (String key : queryParameters.keySet()) {
            if (!key.equals("working.copy.path")) {
                List<String> values = queryParameters.get(key);
                String value = null;
                if (values != null && values.size() > 0) {
                    value = values.get(values.size() - 1);
                }
                parameters.put(key, value);
            }
        }
        String readOnly = parameters.get("read.only");
        String useCache = parameters.get("use.cache");
        String includeVersionControlProperties = parameters.get("include.vcs.properties");
        String merge = parameters.get("merge");
        String failOnValueChange = parameters.get("fail.change");
        TranscriptParameters transcriptParameters = new TranscriptParameters(
                parameters.get("working.copy.path"),
                parameters.get("working.copy.id"),
                readOnly != null && readOnly.equalsIgnoreCase("true"),
                useCache != null && useCache.equalsIgnoreCase("true"),
                includeVersionControlProperties != null && includeVersionControlProperties.equalsIgnoreCase("true"),
                parameters.get("default"),
                merge != null && merge.equalsIgnoreCase("true"),
                failOnValueChange != null && failOnValueChange.equalsIgnoreCase("true"),
                parameters);
        TranscriptModule transcriptModule = new TranscriptModule(transcriptParameters);
        AbstractModule vcsModule;
        String vcs = parameters.get("vcs");
        if (vcs != null && vcs.length() != 0) {
            if (vcs.equals("svn")) {
                String trustServerCertificate = parameters.get("trust.server.certificate");
                SubversionParameters subversionParameters = new SubversionParameters(
                        parameters.get("url"),
                        parameters.get("username"),
                        parameters.get("password"),
                        parameters.get("commit.message"),
                        parameters.get("executable"),
                        trustServerCertificate != null && trustServerCertificate.equalsIgnoreCase("true"));
                vcsModule = new SubversionModule(subversionParameters);
            } else {
                throw new TranscriptException("Specified VCS " + vcs + " is not supported");
            }
        } else {
            throw new TranscriptException("No VCS has been specified");
        }
        return Guice.createInjector(transcriptModule, vcsModule).getInstance(type);
    }

    public TemplateLoader getTemplateloader() {

        return new WebappTemplateLoader(servletContext, "templates");
    }
}
