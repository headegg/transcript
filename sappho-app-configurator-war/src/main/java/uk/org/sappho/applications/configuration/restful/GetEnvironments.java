package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.ConfigurationException;
import uk.org.sappho.applications.configuration.service.Environments;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GetEnvironments {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getEnvironments() throws ConfigurationException {

        return new Environments().getAll();
    }
}
