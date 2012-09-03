/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.Applications;
import uk.org.sappho.applications.configuration.service.ConfigurationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/{environment}")
public class GetApplications {

    @QueryParam("wci") private String workingCopyId;
    @PathParam("environment") private String environment;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getEnvironments() throws ConfigurationException {

        return new Applications(workingCopyId, environment).getAll();
    }
}
