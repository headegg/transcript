/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.restful.transcript.jersey.RestServiceContext;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.Properties;

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
import java.util.SortedMap;
import java.util.TreeMap;

@Path("/{environment}/{application}")
public class PropertiesRestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;
    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SortedMap<String, String> getProperties() throws ConfigurationException {

        return getService().getAllProperties(environment, application, true);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setProperties(final TreeMap<String, String> properties) throws ConfigurationException {

        getService().put(environment, application, properties);
    }

    @DELETE
    public void deleteProperties() throws ConfigurationException {

        getService().delete(environment, application);
    }

    private Properties getService() throws ConfigurationException {

        RestServiceContext<Properties> context = restServiceContextResolver.getContext(Properties.class);
        return context.getService();
    }
}
