/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.restful.transcript.jersey.RestServiceContext;
import uk.org.sappho.applications.services.transcript.registry.Applications;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/{environment}")
public class ApplicationNamesRestService {

    @PathParam("environment")
    private String environment;
    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getApplications() throws ConfigurationException {

        RestServiceContext<Applications> context = restServiceContextResolver.getContext(Applications.class);
        return context.getService().getApplicationNames(environment);
    }
}
