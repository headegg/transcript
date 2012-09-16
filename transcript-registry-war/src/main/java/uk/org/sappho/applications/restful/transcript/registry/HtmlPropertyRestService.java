/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.services.transcript.registry.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/{environment}/{application}/{key}/{defaultValue}")
public class HtmlPropertyRestService extends RestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;
    @PathParam("key")
    private String key;
    @PathParam("defaultValue")
    private String defaultValue;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getProperty() {

        String value = null;
        try {
            value = getService(Properties.class, environment, application).get(key);
        } catch (Throwable throwable) {
        }
        return value != null ? value : (defaultValue != null ? defaultValue : "");
    }
}
