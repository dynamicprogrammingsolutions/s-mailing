package dps.simplemailing.admin;

import dps.router.ControllerBase;
import dps.router.Path;
import dps.simplemailing.entities.*;
import dps.simplemailing.mailqueue.MailGenerator;
import dps.simplemailing.mailqueue.MailQueue;
import dps.simplemailing.mailqueue.MailQueueStatus;
import dps.simplemailing.mailqueue.MailSending;
import dps.simplemailing.manage.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/(.*)")
public class UrlBasedAdminController extends ControllerBase {
    
    @Inject
    UserManager userManager;

    @Inject
    MailManager mailManager;

    @Inject
    MailQueue mailQueue;

    @Inject
    MailGenerator mailGenerator;

    @Inject
    MailSending mailSending;

    @Inject
    SeriesManager mailSeries;

    @Inject
    MailQueueStatus mailQueueStatus;

    @Inject
    SeriesManager seriesManager;

    @Inject
    CampaignManager campaignManager;

    @Path("showUsers")
    public void showUsers(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("show users");
        
        List<User> allUsers = userManager.getAll();
        int cnt = 0;
        for (User user: allUsers) {
            writer.println(user.getId()+" "+user.getEmail()+" "+user.getFirstName()+" "+user.getLastName());
            cnt++;
            if (cnt >= 50) break;
        }
        
    }

    @Path("modifyUser")
    public void modifyUser(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        Long id = Long.parseLong(request.getParameter("id"));
        String firstname = request.getParameter("firstname");
        User user = userManager.getById(id);
        user.setFirstName(firstname);
        userManager.modify(user);

        writer.println("User modified");
    }

    @Path("addMail")
    public void addMail(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("adding mail");
        String name = request.getParameter("name");
        String subject = request.getParameter("subject");
        String from = request.getParameter("from");
        String body = request.getParameter("body");
        Mail mail = new Mail();
        mail.setName(name);
        mail.setSubject(subject);
        mail.setFrom(from);
        mail.setBody_text(body);
        
        writer.println("Name: " + mail.getName());
        writer.println("Subject: " + mail.getSubject());
        writer.println("From: " + mail.getFrom());
        writer.println("Body: " + mail.getBody_text());

        mailManager.create(mail);
        
    }


    @Path("showMails")
    public void showMails(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("Test");
        
        List<Mail> allMails = mailManager.getAll();
        int cnt = 0;
        for (Mail mail: allMails) {
            writer.println(mail.getId()+" "+mail.getName()+" "+mail.getFrom()+" "+mail.getSubject());
            writer.println(mail.getBody_text());
            cnt++;
            if (cnt >= 50) break;
        }
        
    }

    @Path("generateMails")
    public void generateMails(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        List<QueuedMail> queueToSend = mailQueue.getQueueToSend();
        mailQueue.generateMails(queueToSend);
    }

    @Path("processQueue")
    public void processQueue(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        mailQueue.processQueue();
        
        writer.println("queue started");
    }

    @Path("cleanupQueue")
    public void cleanupQueue(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        mailQueue.cleanupQueue();
        
        writer.println("cleaned up");
    }

    @Path("queueStatus")
    public void queueStatus(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
     
        int i = 0;
        while(i < 60) {
            i++;
            writer.println(mailQueueStatus.getStringStatus());
            writer.flush();
            if (!mailQueueStatus.getStarted()) break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UrlBasedAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    @Path("generateMail")
    @Transactional(Transactional.TxType.REQUIRED)
    public void generateMail(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        Long mailId = Long.parseLong(request.getParameter("mail_id"));
        Long userId = Long.parseLong(request.getParameter("user_id"));
        
        User user = userManager.getById(userId);
        Mail mail = mailManager.getById(mailId);
        
        QueuedMail queuedMail = mailQueue.createQueuedMail(user,mail,null);
        
        GeneratedMail generatedMail = mailGenerator.generateMail(queuedMail);
        
    }

    @Path("scheduleMail")
    public void scheduleMail(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {

            Long mailId = Long.parseLong(request.getParameter("mail_id"));
            int delay = Integer.parseInt(request.getParameter("delay"));
            Boolean real = Boolean.parseBoolean(request.getParameter("real"));
            Mail mail = mailManager.getById(mailId);
            
            java.util.Date time = null;
            String timeString = request.getParameter("send_time");
            if (timeString != null) {
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = fm.parse(timeString);
            }

            mailManager.scheduleMail(mail, real, time, delay);

            writer.println("scheduled");

        } catch (ParseException|NumberFormatException ex) {
            Logger.getLogger(UrlBasedAdminController.class.getName()).log(Level.SEVERE, null, ex);
            writer.println("parse failed");
        }
        
    }

    @Path("sendMail")
    public void sendMail(HttpServletRequest request, HttpServletResponse response) {
        Long generatedMailId = Long.parseLong(request.getParameter("generated_id"));
        GeneratedMail generatedMail = mailGenerator.getById(generatedMailId);
        mailSending.sendMail(generatedMail);
    }

    @Path("createSeries")
    public void createSeries(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {
            String name = request.getParameter("name");
            String displayName = request.getParameter("display_name");
            Series series = new Series();
            series.setName(name);
            series.setDisplayName(displayName);
            series.setUpdateSubscribeTime(false);
            seriesManager.create(series);
        } catch (Exception e) {
            writer.println("Failed");
            throw e;
        }
        writer.println("Created");
    }

    @Path("addSeriesItem")
    public void addSeriesItem(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {
            
            Long seriesId = Long.parseLong(request.getParameter("series_id"));
            Long mailId = Long.parseLong(request.getParameter("mail_id"));
            int sendDelay = Integer.parseInt(request.getParameter("send_delay"));
            SeriesItem seriesItem = new SeriesItem();
            seriesItem.setSendDelay(sendDelay);
            seriesManager.createItem(seriesId,mailId,seriesItem);

        } catch (Exception e) {
            writer.println("Failed");
            throw e;
        }
        
        writer.println("Added");
        
    }

    @Path("addSeriesSubscription")
    public void addSeriesSubscription(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {
            
            Long seriesId = Long.parseLong(request.getParameter("series_id"));
            Long userId = Long.parseLong(request.getParameter("user_id"));

            SeriesSubscription seriesSubscription = new SeriesSubscription();
            seriesSubscription.setSubscribeTime(new java.util.Date());

            seriesManager.createSubscription(seriesId,userId,seriesSubscription);

        } catch (Exception e) {
            writer.println("Failed");
            throw e;
        }
        
        writer.println("Added");
        
    }

    @Path("processSeries")
    public void processSeriesUpdated(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {

            Long seriesId = Long.parseLong(request.getParameter("series_id"));
            Series series = seriesManager.getById(seriesId);
            
            mailSeries.processSeries(series);
            
        } catch (Exception e) {
            writer.println("Failed");
            throw e;
        }
        
        writer.println("Processed");
    }

    @Path("processAllSeries")
    public void processAllSeries(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        mailSeries.processAllSeries();
            
        writer.println("Process Started");
    }

    @Path("unsubscribe")
    public void unsubscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {
            String email = request.getParameter("email");
            System.out.println("unsubscribing email "+email);
            User user = userManager.getByEmail(email);
            if (user != null) {
                userManager.unsubscribe(user);
                writer.println("Successfully unsubscribed");
            } else {
                throw new Exception();
            }
        } catch(Exception e) {
            writer.println("Unsubscribe unsuccessful");
        }
    }

    @Path("createCampaign")
    public void createCampaign(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        String name = request.getParameter("name");
        String longName = request.getParameter("long_name");
        
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setLongName(longName);

        campaignManager.create(campaign);

        writer.println("Added, id: "+campaign.getId());
        
    }

    @Path("addMailToCampaign")
    public void addMailToCampaign(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        Long campaignId = Long.parseLong(request.getParameter("campaign_id"));
        Long mailId = Long.parseLong(request.getParameter("mail_id"));

        campaignManager.addMail(campaignId,mailId);

        writer.println("Added");
        
    }

    @Path("removeMailFromCampaign")
    public void removeMailFromCampaign(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        Long campaignId = Long.parseLong(request.getParameter("campaign_id"));
        Long mailId = Long.parseLong(request.getParameter("mail_id"));

        campaignManager.removeMail(campaignId,mailId);

        writer.println("Removed");
        
    }

    @Path("unsubscribeFromCampaign")
    public void unsubscribeFromCampaign(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        try {

            campaignManager.unsubscribeUser(request.getParameter("campaign_name"),request.getParameter("email"));

            writer.println("Successfully unsubscribed from campaign "+request.getParameter("campaign_name"));

        } catch(Exception e) {
            writer.println("Unsubscribe unsuccessful");
        }
    }
}
