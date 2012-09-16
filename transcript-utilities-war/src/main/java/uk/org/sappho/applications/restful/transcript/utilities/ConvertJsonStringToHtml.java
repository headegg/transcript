/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.utilities;

import com.sun.jersey.api.client.Client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/json-str-html")
public class ConvertJsonStringToHtml {

    @QueryParam("url")
    private String url;

    @QueryParam("def")
    private String def;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getProperties() {

        String str = null;
        try {
            str = Client.create().resource(url).get(String.class);
        } catch (Throwable throwable) {
        }
        return str != null ? str : (def != null ? def : "");
    }
}
