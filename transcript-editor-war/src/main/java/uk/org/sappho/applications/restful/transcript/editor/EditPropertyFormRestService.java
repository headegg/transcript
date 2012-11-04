/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.editor;

import uk.org.sappho.applications.restful.transcript.jersey.RestServiceContext;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.Properties;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.net.URI;

@Path("/form/property")
public class EditPropertyFormRestService {

    @FormParam("environment")
    private String environment;
    @FormParam("application")
    private String application;
    @FormParam("key")
    private String key;
    @FormParam("value")
    private String value;
    @FormParam("url")
    private String url;
    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @POST
    public Response setProperty() throws ConfigurationException {

        RestServiceContext<Properties> context = restServiceContextResolver.getContext(Properties.class);
        context.getService().put(environment, application, key, value);
        return Response.seeOther(URI.create(url)).build();
    }
}
