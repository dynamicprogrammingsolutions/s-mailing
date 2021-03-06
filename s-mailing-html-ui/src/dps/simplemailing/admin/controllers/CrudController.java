package dps.simplemailing.admin.controllers;

import dps.authentication.AuthenticationManager;
import dps.authentication.AuthenticationManagerFactory;
import dps.simplemailing.admin.authentication.RestrictedAccess;
import dps.simplemailing.admin.authentication.UsingAuthenticationManager;
import dps.simplemailing.admin.views.Paginator;
import dps.simplemailing.entities.EntityBase;
import dps.simplemailing.admin.views.RequestBean;
import dps.simplemailing.admin.views.SessionBean;
import dps.simplemailing.admin.provider.View;
import dps.simplemailing.manage.ManagerBase;
import dps.simplemailing.rs.Redirect;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.metamodel.Attribute;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Produces(MediaType.TEXT_HTML)
public abstract class CrudController<EntityType extends EntityBase<IdType>,IdType> extends AdminControllerBase implements ControllerInit, UsingAuthenticationManager {

    @Inject
    ManagerBase<EntityType,IdType> manager;

    ManagerBase<EntityType,IdType> getManager()
    {
        return manager;
    }

    @Context
    HttpServletRequest request;

    @Inject
    SessionBean sessionBean;

    @Context
    UriInfo uri;

    protected Attribute<EntityType,?>[] getExtraAttributes()
    {
        Attribute[] arr = {};
        return arr;
    }


    @GET
    @Path("/")
    @RestrictedAccess()
    public View list(@QueryParam("page") Integer page) {

        if (page == null) page = 0;
        int resultsPerPage = sessionBean.getResultsPerPage();
        int firstResult = page*resultsPerPage;
        List<EntityType> items = manager.get(firstResult,resultsPerPage);
        Long count = manager.count();
        Paginator paginator = new Paginator(page,resultsPerPage,count,requestBean.getRoot()+"?page=");
        request.setAttribute("paginator", paginator);
        request.setAttribute("items", items);
        return new View(getViewRoot()+"/list.jsp");
    }

    @GET
    @Path("new")
    @RestrictedAccess()
    public View create(@QueryParam("id") IdType id)
    {
        EntityType entity = null;
        if (id != null) {
            entity = manager.getById(id);
        }
        if (entity == null) {
            entity = manager.newEntity();
        }
        request.setAttribute("formAction",getRoot()+"new");
        request.setAttribute("entity",entity);
        return new View(getViewRoot()+"/new.jsp");
    }

    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RestrictedAccess()
    public Redirect create(EntityType entity)
    {
        try {
            manager.create(entity);
        } catch(IllegalArgumentException e) {
            throw new BadRequestException();
        }
        sessionBean.addMessage("Entity created");
        return new Redirect(getRoot());
    }

    @GET
    @Path("/show/{id}")
    @RestrictedAccess()
    public View show(@PathParam("id") IdType id)
    {
        try {
            EntityType entity = manager.getById(id, getExtraAttributes());
            request.setAttribute("item",entity);
            return new View(getViewRoot()+"/show.jsp");
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("edit/{id}")
    @RestrictedAccess()
    public View edit(@PathParam("id") IdType id)
    {
        EntityType entity = null;
        entity = manager.getById(id);
        request.setAttribute("formAction",getRoot()+"edit/"+id);
        request.setAttribute("entity",entity);
        return new View(getViewRoot()+"/edit.jsp");
    }

    @POST
    @Path("edit/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RestrictedAccess()
    public Redirect edit(@PathParam("id") IdType id, EntityType entity)
    {
        try {
            manager.modify(id,entity);
        } catch(IllegalArgumentException e) {
            throw new BadRequestException();
        }
        sessionBean.addMessage("Entity modified");
        return new Redirect(getRoot());
    }

    @POST
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RestrictedAccess()
    public Redirect delete(@PathParam("id") IdType id)
    {
        try {
            manager.remove(id);
        } catch(EntityNotFoundException e) {
            throw new NotFoundException();
        }
        sessionBean.addMessage("Entity deleted");
        return new Redirect(getRoot());
    }

}
