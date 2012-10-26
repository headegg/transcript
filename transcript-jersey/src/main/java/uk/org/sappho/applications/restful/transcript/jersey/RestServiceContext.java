/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.jersey;

import com.google.inject.Guice;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.TranscriptServiceModule;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Enumeration;

public class RestServiceContext<T> {

    private UriInfo uriInfo;
    private ServletContext servletContext;
    private Class<T> type;

    public RestServiceContext(UriInfo uriInfo, ServletContext servletContext, Class<T> type) {

        this.uriInfo = uriInfo;
        this.servletContext = servletContext;
        this.type = type;
    }

    public T getService() throws ConfigurationException {

        TranscriptServiceModule transcriptServiceModule = new TranscriptServiceModule();
        Enumeration keys = servletContext.getInitParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            transcriptServiceModule.setProperty(key, servletContext.getInitParameter(key));
        }
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        for (String key : queryParameters.keySet()) {
            if (!key.equals("working.copy.path")) {
                transcriptServiceModule.setProperty(key, queryParameters.get(key));
            }
        }
        return Guice.createInjector(transcriptServiceModule.getConfiguredModules()).getInstance(type);
    }
}
