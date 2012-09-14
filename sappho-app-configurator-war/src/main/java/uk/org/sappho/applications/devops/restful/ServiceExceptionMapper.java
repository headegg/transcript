/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.devops.restful;

import uk.org.sappho.applications.devops.service.ConfigurationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<ConfigurationException> {

    public Response toResponse(ConfigurationException exception) {

        return Response.status(404).entity(exception.getMessage() + "\n").type("text/plain").build();
    }
}
