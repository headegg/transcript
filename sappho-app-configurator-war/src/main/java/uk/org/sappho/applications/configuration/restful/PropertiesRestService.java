/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.Properties;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/{environment}/{application}")
public class PropertiesRestService extends RestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getProperties() throws ConfigurationException {

        return getService(Properties.class).getAll(environment, application);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setProperties(LinkedHashMap<String, String> properties) throws ConfigurationException {

        getService(Properties.class).put(environment, application, properties);
    }

    @DELETE
    public void deleteProperties() throws ConfigurationException {

        getService(Properties.class).delete(environment, application);
    }
}
