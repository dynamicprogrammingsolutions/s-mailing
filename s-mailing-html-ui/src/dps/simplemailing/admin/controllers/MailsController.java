package dps.simplemailing.admin.controllers;

import dps.simplemailing.admin.views.Paginator;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.Mail_;
import dps.simplemailing.admin.interceptors.RunInitMethod;
import dps.simplemailing.admin.provider.View;
import dps.simplemailing.manage.CampaignManager;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.rs.Redirect;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityNotFoundException;
import javax.persistence.metamodel.Attribute;
import javax.ws.rs.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path("mails")
@ApplicationScoped
@Interceptors({RunInitMethod.class})
public class MailsController extends CrudController<Mail,Long> {

    @Inject
    CampaignController campaignController;

    @Inject
    CampaignManager campaignManager;

    @Override
    protected String getSubfolder() {
        return "mails";
    }

    @Override
    protected String getTitle()
    {
        return "Mails";
    }

    @Override
    protected Attribute<Mail,?>[] getExtraAttributes()
    {
        Attribute[] arr = {Mail_.campaigns};
        return arr;
    }

    @GET
    @Path("show/{id}/add_to_campaign")
    public View addToCampaign(@PathParam("id") Long id, @QueryParam("page") Integer page)
    {
        View result = campaignController.list(page);

        requestBean.setRoot(getRoot()+"show/"+id+"/add_to_campaign");

        Paginator paginator = (Paginator)request.getAttribute("paginator");
        paginator.setPrefix(requestBean.getRoot()+"?page=");

        return new View(getViewRoot()+"/addToCampaign.jsp");
    }

    @POST
    @Path("show/{id}/add_to_campaign")
    public Redirect addToCampaignPost(@PathParam("id") Long id, @FormParam("id") Long campaignId)
    {
        System.out.println("adding "+id+" to "+campaignId);
        campaignManager.addMail(campaignId,id);
        sessionBean.addMessage("Mail added to campaign");
        return new Redirect(getRoot()+"show/"+id);
    }

    @POST
    @Path("show/{id}/delete_from_campaign")
    public Redirect postDeleteFromCampaign(@PathParam("id") Long id, @FormParam("id") Long campaignId)
    {
        System.out.println("adding "+id+" to "+campaignId);
        campaignManager.removeMail(campaignId,id);
        sessionBean.addMessage("Mail deleted from campaign");
        return new Redirect(getRoot()+"show/"+id);
    }

    @GET
    @Path("schedule/{id}")
    public View schedule(@PathParam("id") Long id)
    {
        try {
            Mail mail = manager.getById(id);
            request.setAttribute("item", mail);
            return new View(getViewRoot()+"/schedule.jsp");
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @POST
    @Path("schedule/{id}")
    public Redirect schedule(@PathParam("id") Long id, @FormParam("send_time") String sendTime, @FormParam("real") String strReal)
    {
        Mail mail;
        try {
            mail = manager.getById(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
        Boolean real = "on".equals(strReal);
        Date time = null;
        String timeString = request.getParameter("send_time");
        if (timeString != null && !timeString.isEmpty()) {
            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                time = fm.parse(timeString);
            } catch (ParseException e) {
                throw new BadRequestException();
            }
        }
        ((MailManager)manager).scheduleMail(mail,real,time,0);
        sessionBean.addMessage("Mail scheduled");
        return new Redirect(getRoot());
    }



}
