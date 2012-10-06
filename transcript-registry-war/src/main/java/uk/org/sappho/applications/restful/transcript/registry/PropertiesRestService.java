/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.restful.transcript.jersey.AbstractRestService;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/{environment}/{application}")
public class PropertiesRestService extends AbstractRestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getProperties() throws ConfigurationException {

        return getService().getInstance(Properties.class).getAllProperties(environment, application, true);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setProperties(LinkedHashMap<String, String> properties) throws ConfigurationException {

        getService().getInstance(Properties.class).put(environment, application, properties);
    }

    @DELETE
    public void deleteProperties() throws ConfigurationException {

        getService().getInstance(Properties.class).delete(environment, application);
    }
}
