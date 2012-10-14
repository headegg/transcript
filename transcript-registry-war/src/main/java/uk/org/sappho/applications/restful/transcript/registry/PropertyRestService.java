/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.restful.transcript.jersey.RestService;
import uk.org.sappho.applications.restful.transcript.jersey.RestSession;
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

@Path("/{environment}/{application}/{key}")
public class PropertyRestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;
    @PathParam("key")
    private String key;
    @Context
    private ContextResolver<RestService> restServiceContextResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProperty() throws ConfigurationException {

        final RestService<Properties> restService = restServiceContextResolver.getContext(Properties.class);
        RestSession.Action<String> action = new RestSession.Action<String>() {
            private String value;

            @Override
            public void execute() throws ConfigurationException {
                value = restService.getService().get(environment, application, key, true);
            }

            @Override
            public String getResponse() {
                return value;
            }
        };
        restService.getSession().execute(action);
        return action.getResponse();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setProperty(final String value) throws ConfigurationException {

        final RestService<Properties> restService = restServiceContextResolver.getContext(Properties.class);
        RestSession.Action<Object> action = new RestSession.Action<Object>() {
            @Override
            public void execute() throws ConfigurationException {
                restService.getService().put(environment, application, key, value);
            }

            @Override
            public Object getResponse() {
                return null;
            }
        };
        restService.getSession().execute(action);
    }

    @DELETE
    public void deleteProperty() throws ConfigurationException {

        final RestService<Properties> restService = restServiceContextResolver.getContext(Properties.class);
        RestSession.Action<Object> action = new RestSession.Action<Object>() {
            @Override
            public void execute() throws ConfigurationException {
                restService.getService().delete(environment, application, key);
            }

            @Override
            public Object getResponse() {
                return null;
            }
        };
        restService.getSession().execute(action);
    }
}
