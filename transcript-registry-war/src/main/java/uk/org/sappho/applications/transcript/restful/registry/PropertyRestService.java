/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.registry;

import uk.org.sappho.applications.transcript.restful.jersey.RestServiceContext;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.registry.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/{environment}/{application}/{key}")
public class PropertyRestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;
    @PathParam("key")
    private String key;
    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public String getProperty() throws TranscriptException {

        return getService().get(environment, application, key);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public void setProperty(final String value) throws TranscriptException {

        getService().put(environment, application, key, value);
    }

    @DELETE
    public void deleteProperty() throws TranscriptException {

        getService().delete(environment, application, key);
    }

    private Properties getService() throws TranscriptException {

        RestServiceContext<Properties> context = restServiceContextResolver.getContext(Properties.class);
        return context.getService();
    }
}
