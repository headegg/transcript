/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.report;

import uk.org.sappho.applications.transcript.restful.jersey.RestServiceContext;
import uk.org.sappho.applications.transcript.service.TranscriptException;
import uk.org.sappho.applications.transcript.service.report.TextTemplatedReport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/text/{templateName}")
public class TextReportRestService {

    @PathParam("templateName")
    private String templateName;
    @Context
    private ContextResolver<RestServiceContext> restServiceContextResolver;

    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String getProperty() throws TranscriptException {

        RestServiceContext<TextTemplatedReport> context =
                restServiceContextResolver.getContext(TextTemplatedReport.class);
        return context.getService().generate(templateName, context.getTemplateloader());
    }
}
