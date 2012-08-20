package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.util.Map;

@Path("/{environment}/{application}/{key}")
public class GetProperty {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEnvironments(@PathParam("environment") String environment,
                                               @PathParam("application") String application,
                                               @PathParam("key") String key) throws ConfigurationException, FileNotFoundException {

        return new Properties(environment, application).get(key);
    }
}
