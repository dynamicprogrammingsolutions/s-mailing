package dps.webapplication.application.admin;


import dps.webapplication.application.providers.View;
import dps.webapplication.application.providers.annotations.AllowedRoles;
import dps.webapplication.application.providers.annotations.NotAuthorizedRedirect;
import dps.webapplication.authentication.CurrentAuthenticationManager;
import dps.webapplication.entities.ApplicationUser;
import dps.webapplication.messages.Messages;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("admin/account")
@AllowedRoles("nodemo")
public class Account {

    @Inject
    CurrentAuthenticationManager authenticationManager;

    @Inject
    Messages messages;

    @PersistenceContext(unitName = "DefaultPersistenceUnit")
    EntityManager em;

    @GET
    @Path("/")
    @NotAuthorizedRedirect("admin/auth/login")
    @Produces(MediaType.TEXT_HTML)
    public View index(@Context HttpServletRequest request) {
        request.setAttribute("userData",authenticationManager.getUser());
        return new View("/WEB-INF/admin/settings/account.jsp");
    }

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public View modify(@Context HttpServletRequest request, @FormParam("currentPassword") String currentPassword, @FormParam("password") String password) {

        ApplicationUser user = (ApplicationUser)authenticationManager.getUser();
        if (password != null && !password.isEmpty() && currentPassword != null && !currentPassword.isEmpty()) {
            if (!user.checkCredentials(user.getUsername(), currentPassword)) {
                messages.addMessage(Messages.Type.Error,"A megadott jelenlegi jelszó hibás");
                return index(request);
            } else {
                user.setPassword(password);
                try {
                    em.merge(user);
                    messages.addMessage(Messages.Type.Success,"A jelszó sikeresen módosítva lett");
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        }
        return index(request);
    }

}
