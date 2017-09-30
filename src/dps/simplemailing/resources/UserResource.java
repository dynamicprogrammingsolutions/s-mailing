package dps.simplemailing.resources;

import dps.simplemailing.entities.User;
import dps.simplemailing.manage.ManagerBase;
import dps.simplemailing.manage.UserManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.Path;

@Dependent
@Path("users")
public class UserResource extends ResourceBase<User,Long> {

}
