/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import dps.simplemailing.back.Crud;
import dps.simplemailing.back.Crud;
import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Mail;
import java.util.ArrayList;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Dependent
@Path("mails")
public class MailResource {
    
    @Inject Crud crud;
    
    @GET
    @Path("test")
    public String test()
    {
        return "Test Success!";
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mail> getMail()
    {
        return crud.getAll(Mail.class);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public void newMail(Mail mail)
    {
        crud.create(mail);
    }
        
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mail getMail(@PathParam("id") Long id)
    {
        return crud.find(id, Mail.class);
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public void editMail(@PathParam("id") Long id, Mail editedMail)
    {
        Mail mail = crud.find(id, Mail.class);
        if (mail == null) throw new javax.ws.rs.NotFoundException();
        editedMail.setId(mail.getId());
        crud.edit(editedMail);
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional(Transactional.TxType.REQUIRED)
    public void editMail(@PathParam("id") Long id)
    {
        Mail mail = crud.find(id, Mail.class);
        if (mail == null) throw new javax.ws.rs.NotFoundException();
        crud.remove(mail);
    }
    
    @GET
    @Path("/{id}/campaigns")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public List<Campaign> getMailCampaigns(@PathParam("id") Long id)
    {
        Mail mail = crud.find(id, Mail.class);
        if (mail == null) throw new NotFoundException();
        List<Campaign> campaigns = new ArrayList<>();
        for (Campaign campaign: mail.getCampaigns()) {
            campaigns.add(campaign);
        }
        return campaigns;
    }

    @PUT
    @Path("/{id}/campaigns/{campaignId}")
    @Transactional(Transactional.TxType.REQUIRED)
    public void addMailToCampaign(@PathParam("id") Long id, @PathParam("campaignId") Long campaignId)
    {
        Mail mail = crud.find(id, Mail.class);
        if (mail == null) throw new NotFoundException();
        Campaign campaign = crud.find(campaignId,Campaign.class);
        if (campaign == null) throw new NotFoundException();
        campaign.getMails().add(mail);
        crud.edit(campaign);
    }
    
}
