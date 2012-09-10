/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/{environment}/{application}")
public class GetProperties extends RestService {

    @PathParam("environment")
    private String environment;
    @PathParam("application")
    private String application;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getEnvironments() throws ConfigurationException {

        return getService(Properties.class).getAll(environment, application);
    }
}
