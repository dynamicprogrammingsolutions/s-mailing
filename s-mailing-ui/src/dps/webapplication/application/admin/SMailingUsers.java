package dps.webapplication.application.admin;

import dps.simplemailing.entities.User;
import dps.simplemailing.manage.ManagerBase;
import dps.simplemailing.manage.UserManager;
import dps.webapplication.application.providers.View;
import dps.webapplication.application.providers.annotations.NotAuthorizedRedirect;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Dependent
@Path("admin/s-mailing/users")
public class SMailingUsers extends SMailingCRUD<User,Long> {

    @Inject
    UserManager userManager;

    ManagerBase<User,Long> getManager() {
        return userManager;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @NotAuthorizedRedirect("admin/auth/login")
    public View index()
    {
        return new View("/WEB-INF/admin/s-mailing/users.jsp");
    }

}
