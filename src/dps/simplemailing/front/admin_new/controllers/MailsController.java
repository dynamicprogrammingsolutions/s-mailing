package dps.simplemailing.front.admin_new.controllers;

import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.Mail_;
import dps.simplemailing.front.admin_new.interceptors.RunInitMethod;
import dps.simplemailing.front.admin_new.provider.View;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.rs.Redirect;

import javax.enterprise.context.ApplicationScoped;
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
