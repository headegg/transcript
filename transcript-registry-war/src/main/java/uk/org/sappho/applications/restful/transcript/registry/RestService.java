/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.ServiceModule;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Enumeration;

public class RestService {

    @Context
    private UriInfo uriInfo;
    @Context
    private ServletContext servletContext;

    protected <T> T getService(Class<T> type, String environment, String application) throws ConfigurationException {

        ServiceModule serviceModule = new ServiceModule(environment, application);
        Enumeration keys = servletContext.getInitParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            serviceModule.setProperty(key, servletContext.getInitParameter(key));
        }
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        for (String key : queryParameters.keySet()) {
            if (!key.equals("working.copy.path")) {
                serviceModule.setProperty(key, queryParameters.get(key));
            }
        }
        return serviceModule.getInjector().getInstance(type);
    }
}
