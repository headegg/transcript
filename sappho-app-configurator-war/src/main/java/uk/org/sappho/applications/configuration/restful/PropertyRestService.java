/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/{environment}/{application}/{key}")
public class PropertyRestService extends RestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;
    @PathParam("key")
    private String key;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProperty() throws ConfigurationException {

        return properties().get(key);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setProperty(String value) throws ConfigurationException {

        properties().put(key, value);
    }

    @DELETE
    public void deleteProperty() throws ConfigurationException {

        properties().delete(key);
    }

    private Properties properties() throws ConfigurationException {

        return getService(Properties.class, environment, application);
    }
}
