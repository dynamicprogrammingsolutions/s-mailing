package dps.simplemailing.resources;

import dps.simplemailing.entities.User;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Dependent
@Path("users")
public class UserResource extends ResourceBase<User,Long> {

    /*@GET
    @Path("/")
    @Produces("text/plain")
    public String test()
    {
        return "TEST OK";
    }*/

}
