/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.Applications;
import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.Environments;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/{environment}")
public class EnvironmentRestService extends RestService {

    @PathParam("environment")
    private String environment;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getApplications() throws ConfigurationException {

        return getService(Applications.class).getAll(environment);
    }
}
