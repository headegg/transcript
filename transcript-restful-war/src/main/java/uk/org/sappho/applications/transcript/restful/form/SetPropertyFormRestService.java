/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.form;

import uk.org.sappho.applications.transcript.restful.jersey.RestServiceContext;
import uk.org.sappho.applications.transcript.service.registry.ConfigurationException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.net.URI;

@Path("/form/property")
public class SetPropertyFormRestService {

    @FormParam("environment")
    private String environment;
    @FormParam("application")
    private String application;
    @FormParam("key")
    private String key;
    @FormParam("value")
    private String value;
    @FormParam("commitMessage")
    private String commitMessage;
    @FormParam("acknowledgeUrl")
    private String acknowledgeUrl;
    @FormParam("errorUrl")
    private String errorUrl;
    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @POST
    public Response setProperty() throws ConfigurationException {

        RestServiceContext<PropertiesFromForm> context =
                restServiceContextResolver.getContext(PropertiesFromForm.class);
        return Response.seeOther(URI.create(context.getService().put(environment, application, key, value,
                commitMessage, acknowledgeUrl, errorUrl))).build();
    }
}
