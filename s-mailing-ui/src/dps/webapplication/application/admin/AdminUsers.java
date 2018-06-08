package dps.webapplication.application.admin;


import dps.webapplication.application.providers.View;
import dps.webapplication.application.providers.annotations.AllowedRoles;
import dps.webapplication.application.providers.annotations.NotAuthorizedRedirect;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("admin/users")
@AllowedRoles("super")
public class AdminUsers {

    @PersistenceContext(unitName = "DefaultPersistenceUnit")
    EntityManager em;

    @Inject
    AdminUsersCRUD adminUsersCRUD;

    @Path("/rest")
    public AdminUsersCRUD crud()
    {
        return adminUsersCRUD;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @NotAuthorizedRedirect("admin/auth/login")
    public View index()
    {
        return new View("/WEB-INF/admin/users.jsp");
    }


}
