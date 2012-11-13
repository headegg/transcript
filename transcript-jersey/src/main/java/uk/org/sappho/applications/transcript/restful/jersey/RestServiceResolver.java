/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.jersey;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class RestServiceResolver implements ContextResolver<RestServiceContext> {

    @Context
    private UriInfo uriInfo;
    @Context
    private ServletContext servletContext;

    @Override
    public RestServiceContext getContext(Class<?> type) {

        return new RestServiceContext(uriInfo, servletContext, type);
    }
}
