/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.restful.transcript.jersey.RestServiceContext;
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
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getProperty() throws ConfigurationException {

        RestServiceContext<Properties> context = restServiceContextResolver.getContext(Properties.class);
        String value = null;
        try {
            value = context.getService().get(environment, application, key, true);
        } catch (Throwable throwable) {
        }
        return value != null ? value : (defaultValue != null ? defaultValue : "");
    }
}
