/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.ServiceModule;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class RestService {

    @Context
    private UriInfo uriInfo;

    protected <T> T getService(Class<T> type) throws ConfigurationException {

        ServiceModule serviceModule = new ServiceModule();
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        for (String key : queryParameters.keySet()) {
            serviceModule.setProperty(key, queryParameters.get(key));
        }
        return serviceModule.getInjector().getInstance(type);
    }
}
