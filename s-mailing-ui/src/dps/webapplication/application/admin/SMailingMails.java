package dps.webapplication.application.admin;

import dps.simplemailing.entities.Mail;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.manage.ManagerBase;
import dps.webapplication.application.providers.View;
import dps.webapplication.application.providers.annotations.NotAuthorizedRedirect;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Dependent
@Path("admin/s-mailing/mails")
public class SMailingMails extends SMailingCRUD<Mail,Long> {

    @Inject
    MailManager mailManager;

    ManagerBase<Mail, Long> getManager() {
        return mailManager;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @NotAuthorizedRedirect("admin/auth/login")
    public View index() {
        return new View("/WEB-INF/admin/s-mailing/mails.jsp");
    }

    @POST
    @Path("/rest/{id}/schedule")
    public void scheduleMail(@PathParam("id") Long mailId, ScheduleMail schedule)
    {
        Mail mail;
        try {
            try {
                mail = getManager().getById(mailId);
                if (!Objects.equals(mailId, schedule.id)) {
                    throw new BadRequestException();
                }
            } catch (EntityNotFoundException e) {
                throw new NotFoundException(e);
            }
            //System.out.println(schedule.getReal()+" "+schedule.getTime());
            mailManager.scheduleMail(mail,schedule.getReal(),schedule.getTime(),0);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
    }

    public static class ScheduleMail {
        Long id;
        Boolean real;

        @XmlJavaTypeAdapter(DateAdapter.class)
        Date time;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Boolean getReal() {
            return real;
        }

        public void setReal(Boolean real) {
            this.real = real;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

    static public class DateAdapter extends XmlAdapter<String,Date> {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        @Override
        public Date unmarshal(String v) throws Exception {
            try {
                return dateFormat.parse(v);
            } catch (ParseException e) {
                throw new UnmarshalException(e);
            }
        }

        @Override
        public String marshal(Date v) throws Exception {
            return dateFormat.format(v);
        }
    }



}
