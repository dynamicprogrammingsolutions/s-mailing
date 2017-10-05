package dps.simplemailing.resources;

import dps.simplemailing.entities.User;

import javax.enterprise.context.Dependent;
import javax.ws.rs.Path;

@Dependent
@Path("users")
public class UserResource extends ResourceBase<User,Long> {

}
