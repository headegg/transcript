/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.registry;

import uk.org.sappho.applications.restful.transcript.jersey.RestService;
import uk.org.sappho.applications.restful.transcript.jersey.RestSession;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.Environments;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/")
public class EnvironmentNamesRestService {

    @Context
    private ContextResolver<RestService> restServiceContextResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getEnvironments() throws ConfigurationException {

        final RestService<Environments> restService = restServiceContextResolver.getContext(Environments.class);
        RestSession.Action<String[]> action = new RestSession.Action<String[]>() {
            private String[] environmentNames;

            @Override
            public void execute() throws ConfigurationException {
                environmentNames = restService.getService().getEnvironmentNames();
            }

            @Override
            public String[] getResponse() {
                return environmentNames;
            }
        };
        restService.getSession().execute(action);
        return action.getResponse();
    }
}
