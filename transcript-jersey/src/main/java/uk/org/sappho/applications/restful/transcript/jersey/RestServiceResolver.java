/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.jersey;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class RestServiceResolver implements ContextResolver<RestService> {

    @Context
    private UriInfo uriInfo;
    @Context
    private ServletContext servletContext;
    private RestSession restSession = new RestSession();

    @Override
    public RestService getContext(Class<?> type) {

        return new RestService(uriInfo, servletContext, type, restSession);
    }
}
