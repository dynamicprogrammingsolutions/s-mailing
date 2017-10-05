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
@Path("mails")
public class MailResource extends ResourceBase<Mail,Long> {

    @Inject
    MailManager mailManager;

    @GET
    @Path("/{id}/campaigns")
    public Response getMailCampaigns(@PathParam("id") Long id)
    {
        Set<Campaign> campaigns = mailManager.getById(id,"campaigns").getCampaigns();
        if (campaigns.isEmpty()) return Response.noContent().build();
        else return Response.ok(campaigns).build();
    }

    /*
    @PUT
    @Path("/{id}/campaigns/{campaignId}")
    public void addMailToCampaign(@PathParam("id") Long id, @PathParam("campaignId") Long campaignId)
    {
        try {
            mailManager.addMailToCampaign(id, campaignId);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException();
        }
    }
    */
}
