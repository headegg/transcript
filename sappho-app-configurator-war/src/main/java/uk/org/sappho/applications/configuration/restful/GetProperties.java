package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/{environment}/{application}")
public class GetProperties {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getEnvironments(@PathParam("environment") String environment,
                                               @PathParam("application") String application) throws ConfigurationException {

        return new Properties(environment, application).getAll();
    }
}
