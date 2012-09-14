/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.devops.deployment.restful;

import uk.org.sappho.applications.devops.service.Applications;
import uk.org.sappho.applications.devops.service.ConfigurationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/{environment}")
public class EnvironmentRestService extends RestService {

    @PathParam("environment")
    private String environment;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getApplications() throws ConfigurationException {

        return getService(Applications.class, environment, ".").getAll(environment);
    }
}
