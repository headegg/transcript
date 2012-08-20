package uk.org.sappho.applications.configuration.restful;

import uk.org.sappho.applications.configuration.service.Applications;
import uk.org.sappho.applications.configuration.service.ConfigurationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Vector;

@Path("/{environment}")
public class GetApplications {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getEnvironments(@PathParam("environment") String environment) throws ConfigurationException {

        return new Applications(environment).get();
    }
}
