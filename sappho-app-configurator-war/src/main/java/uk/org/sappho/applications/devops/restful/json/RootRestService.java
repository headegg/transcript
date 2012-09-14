/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.devops.restful.json;

import uk.org.sappho.applications.devops.restful.RestService;
import uk.org.sappho.applications.devops.service.ConfigurationException;
import uk.org.sappho.applications.devops.service.Environments;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RootRestService extends RestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getEnvironments() throws ConfigurationException {

        return getService(Environments.class, ".", ".").getAll();
    }
}
