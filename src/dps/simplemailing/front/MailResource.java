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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.jboss.resteasy.plugins.providers.html.View;


@Dependent
@Path("mails")
public class MailResource {
    
    @Inject Crud crud;
    @Context HttpServletRequest request;
    @Context HttpServletResponse response;
    @Context ServletContext servletContext;
    
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() throws ServletException, IOException
    {
        return "Test Success "+request.getContextPath();
    }
   
    
    @GET
    @Path("testjsp")
    @Produces(MediaType.TEXT_HTML)
    public View getJspTest(@Context HttpServletRequest request, 
                        @Context HttpServletResponse response) throws ServletException, IOException {
        System.out.println("running");
        
        return new View("/WEB-INF/testjsp.jsp");
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
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public Mail newMail(Mail mail)
    {
        crud.create(mail);
        return mail;
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
        if (mail == null) throw new NotFoundException();
        editedMail.setId(mail.getId());
        crud.edit(editedMail);
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteMail(@PathParam("id") Long id)
    {
        Mail mail = crud.find(id, Mail.class);
        if (mail == null) throw new NotFoundException();
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
