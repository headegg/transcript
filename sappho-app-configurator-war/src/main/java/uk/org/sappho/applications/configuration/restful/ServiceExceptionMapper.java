package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<ConfigurationException> {

    public Response toResponse(ConfigurationException exception) {

        return Response.status(404).entity("ERROR! " + exception.getMessage()).type("text/plain").build();
    }
}
