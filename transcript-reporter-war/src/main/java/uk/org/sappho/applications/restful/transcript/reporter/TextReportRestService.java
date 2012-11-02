/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.reporter;

import freemarker.cache.WebappTemplateLoader;
import uk.org.sappho.applications.restful.transcript.jersey.RestServiceContext;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/text/{templateName}")
public class TextReportRestService {

    @QueryParam("environments")
    private String environments;
    @QueryParam("applications")
    private String applications;
    @QueryParam("keys")
    private String keys;
    @PathParam("templateName")
    private String templateName;
    @QueryParam("include.vcs.props")
    private boolean includeVersionControlProperties;
    @QueryParam("include.undefined.environments")
    private boolean includeUndefinedEnvironments;
    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;
    @Context
    private ServletContext servletContext;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getProperty() throws ConfigurationException {

        RestServiceContext<TextTemplatedReport> context =
                restServiceContextResolver.getContext(TextTemplatedReport.class);
        String[] environmentList = null;
        if (environments != null && environments.length() != 0) {
            environmentList = environments.split(",");
        }
        String[] applicationList = null;
        if (applications != null && applications.length() != 0) {
            applicationList = applications.split(",");
        }
        String[] keyList = null;
        if (keys != null && keys.length() != 0) {
            keyList = keys.split(",");
        }
        return context.getService().generate(templateName, environmentList, applicationList, keyList,
                includeVersionControlProperties, includeUndefinedEnvironments,
                new WebappTemplateLoader(servletContext, "templates"));
    }
}
