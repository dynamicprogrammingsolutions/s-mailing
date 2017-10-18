package dps.simplemailing.admin.controllers;

import dps.simplemailing.admin.authentication.RestrictedAccess;
import dps.simplemailing.admin.interceptors.AuthenticationInterceptor;
import dps.simplemailing.admin.provider.View;
import dps.simplemailing.admin.views.Paginator;
import dps.simplemailing.entities.Series;
import dps.simplemailing.entities.SeriesItem;
import dps.simplemailing.entities.Series_;
import dps.simplemailing.admin.interceptors.RunInitMethod;
import dps.simplemailing.manage.SeriesItemManager;
import dps.simplemailing.manage.SeriesManager;
import dps.simplemailing.rs.Redirect;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityNotFoundException;
import javax.persistence.metamodel.Attribute;
import javax.ws.rs.*;

@Path("series")
@ApplicationScoped
@Interceptors({RunInitMethod.class, AuthenticationInterceptor.class})
public class SeriesController extends CrudController<Series,Long> {

    @Override
    protected String getSubfolder() {
        return "series";
    }

    @Override
    protected String getTitle()
    {
        return "Series";
    }

    @Override
    protected Attribute<Series,?>[] getExtraAttributes()
    {
        Attribute[] arr = {Series_.seriesItems};
        return arr;
    }

    @Inject
    MailsController mailsController;

    @Inject
    SeriesManager seriesManager;

    @Inject
    SeriesItemManager seriesItemManager;

    @GET
    @Path("{id}/add_mail")
    @RestrictedAccess()
    public View addMail(@PathParam("id") Long id, @QueryParam("page") Integer page)
    {
        mailsController.list(page);

        requestBean.setRoot(getRoot()+id+"/add_mail");

        Paginator paginator = (Paginator)request.getAttribute("paginator");
        paginator.setPrefix(requestBean.getRoot()+"?page=");

        return new View(getViewRoot()+"/addMail.jsp");

    }

    @POST
    @Path("{id}/add_mail")
    @RestrictedAccess()
    public Redirect postAddMail(@PathParam("id") Long id, @FormParam("id") Long mailId)
    {
        System.out.println("adding "+mailId+" to "+id);
        SeriesItem seriesItem = new SeriesItem();
        seriesManager.createItem(id,mailId,seriesItem);
        sessionBean.addMessage("Mail added to series");
        return new Redirect(getRoot()+"show/"+id);
    }

    @GET
    @Path("{id}/items/show/{itemId}")
    @RestrictedAccess()
    public View showItem(@PathParam("id") Long id, @PathParam("itemId") Long itemId)
    {
        try {
            Series series = manager.getById(id, getExtraAttributes());
            requestBean.setRoot(getRoot()+id+"/items/");
            SeriesItem item = null;
            for (SeriesItem item_: series.getSeriesItems()) {
                if (item_.getId().equals(itemId)) item = item_;
            }
            if (item == null) throw new NotFoundException();
            request.setAttribute("item",item);
            System.out.println("view: "+getViewRoot()+"/items/show.jsp");
            return new View(getViewRoot()+"/items/show.jsp");
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("{id}/items/edit/{itemId}")
    @RestrictedAccess()
    public View editItem(@PathParam("id") Long id, @PathParam("itemId") Long itemId)
    {
        Series series = manager.getById(id, getExtraAttributes());
        requestBean.setRoot(getRoot()+id+"/items/");
        SeriesItem entity = null;
        for (SeriesItem item: series.getSeriesItems()) {
            if (item.getId().equals(itemId)) entity = item;
        }
        if (entity == null) throw new NotFoundException();

        System.out.println("entity id: "+entity.getId());
        request.setAttribute("formAction",requestBean.getRoot()+"edit/"+itemId);
        request.setAttribute("entity",entity);
        return new View(getViewRoot()+"/items/edit.jsp");
    }


    @POST
    @Path("{id}/items/edit/{itemId}")
    @RestrictedAccess()
    public Redirect editItem(@PathParam("id") Long id, @PathParam("itemId") Long itemId, SeriesItem modified) {
        Series series = manager.getById(id, getExtraAttributes());
        requestBean.setRoot(getRoot()+id+"/items/");
        SeriesItem entity = null;
        for (SeriesItem item: series.getSeriesItems()) {
            if (item.getId().equals(itemId)) entity = item;
        }
        if (entity == null) throw new NotFoundException();

        modified.setMail(entity.getMail());
        modified.setSeries(entity.getSeries());

        try {
            seriesItemManager.modify(itemId,modified);
        } catch(IllegalArgumentException e) {
            throw new BadRequestException();
        }
        sessionBean.addMessage("Entity modified");
        return new Redirect(getRoot()+"show/"+id);

    }

}
