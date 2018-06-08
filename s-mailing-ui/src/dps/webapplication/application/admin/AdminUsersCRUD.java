package dps.webapplication.application.admin;

import dps.webapplication.application.CrudBase;
import dps.webapplication.application.providers.annotations.AllowedRoles;
import dps.webapplication.entities.ApplicationUser;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Dependent
@AllowedRoles("super")
public class AdminUsersCRUD extends CrudBase<ApplicationUser, Long, ApplicationUser, ApplicationUser> {

    @PersistenceContext(unitName = "DefaultPersistenceUnit")
    EntityManager em;

    protected EntityManager getEM() {
        return em;
    }

    @Context
    HttpServletRequest request;

    @Override
    protected void processBeforeEdit(ApplicationUser originalEntity, ApplicationUser editedEntity) {
        if (editedEntity.getPasswordHash() == null) {
            editedEntity.setPasswordHash(originalEntity.getPasswordHash());
        }
    }
}
