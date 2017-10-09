package dps.simplemailing.resources;

import dps.simplemailing.manage.MailManager;
import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Mail;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Set;

@Dependent
@Path("resources/mails")
public class MailResource extends ResourceBase<Mail,Long> {

    @Inject
    MailManager mailManager;

    @Path("/ok")
    @Produces("text/plain")
    @GET
    public String test()
    {
        return "TEST OK";
    }

    @GET
    @Path("/{id}/campaigns")
    public Response getMailCampaigns(@PathParam("id") Long id)
    {
        Set<Campaign> campaigns = mailManager.getById(id,"campaigns").getCampaigns();
        if (campaigns.isEmpty()) return Response.noContent().build();
        else return Response.ok(campaigns).build();
    }

}
