/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.restful.transcript.jersey.RestService;
import uk.org.sappho.applications.restful.transcript.jersey.RestSession;
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
    private ContextResolver<RestService> restServiceContextResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getApplications() throws ConfigurationException {

        final RestService<Applications> restService = restServiceContextResolver.getContext(Applications.class);
        RestSession.Action<String[]> action = new RestSession.Action<String[]>() {
            private String[] applicationNames;

            @Override
            public void execute() throws ConfigurationException {
                applicationNames = restService.getService().getApplicationNames(environment);
            }

            @Override
            public String[] getResponse() {
                return applicationNames;
            }
        };
        restService.getSession().execute(action);
        return action.getResponse();
    }
}
