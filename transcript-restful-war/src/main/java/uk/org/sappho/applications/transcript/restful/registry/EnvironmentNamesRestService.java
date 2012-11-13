/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.registry;

import uk.org.sappho.applications.transcript.restful.jersey.RestServiceContext;
import uk.org.sappho.applications.transcript.service.registry.ConfigurationException;
import uk.org.sappho.applications.transcript.service.registry.Environments;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/registry")
public class EnvironmentNamesRestService {

    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getEnvironments() throws ConfigurationException {

        RestServiceContext<Environments> context = restServiceContextResolver.getContext(Environments.class);
        return context.getService().getEnvironmentNames();
    }
}
