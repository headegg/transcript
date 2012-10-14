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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/{environment}/{application}/{key}/{defaultValue}")
public class HtmlPropertyRestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;
    @PathParam("key")
    private String key;
    @PathParam("defaultValue")
    private String defaultValue;
    @Context
    private ContextResolver<RestService> restServiceContextResolver;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getProperty() throws ConfigurationException {

        final RestService<Properties> restService = restServiceContextResolver.getContext(Properties.class);
        RestSession.Action<String> action = new RestSession.Action<String>() {
            private String value = null;

            @Override
            public void execute() {
                try {
                    value = restService.getService().get(environment, application, key, true);
                } catch (Throwable throwable) {
                }
            }

            @Override
            public String getResponse() {
                return value != null ? value : (defaultValue != null ? defaultValue : "");
            }
        };
        restService.getSession().execute(action);
        return action.getResponse();
    }
}
