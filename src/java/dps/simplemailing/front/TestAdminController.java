/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.ControllerBase;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Path;
import dps.simplemailing.back.Campaigns;
import dps.simplemailing.back.Crud;
import dps.simplemailing.back.MailGenerator;
import dps.simplemailing.back.MailQueue;
import dps.simplemailing.back.MailQueueStatus;
import dps.simplemailing.back.MailSending;
import dps.simplemailing.back.MailSeries;
import dps.simplemailing.back.Mails;
import dps.simplemailing.back.Users;
import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.entities.Series;
import dps.simplemailing.entities.SeriesItem;
import dps.simplemailing.entities.SeriesSubscription;
import dps.simplemailing.entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
@Path("/(.*)")
public class TestAdminController extends AdminControllerBase {
    
    @Inject Users userManager;
    @Inject Mails mailManager;
    @Inject MailQueue mailQueue;
    @Inject MailGenerator generatedMails;
    @Inject MailSending mailSending;
    @Inject MailSeries mailSeries;
    @Inject MailQueueStatus mailQueueStatus;
    @Inject Campaigns campaigns;
    
    @Inject Crud crud;
    
    @Filter
    @Override
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, InvocationTargetException, ServletException, IllegalArgumentException
    {
        requestBean.setTitle("S-Mailing - Mails");
        requestBean.setRoot(request.getContextPath()+request.getServletPath()+"/mails/"); 
        requestBean.setTemplate("/WEB-INF/templates/template.jsp");

        super.filter(request, response, controller, method, args);

    }
    
    @Path("")
    public String index()
    {
        return "/WEB-INF/admin/index.jsp";
    }
    
    @Path("showUsers")
    public void showUsers(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("show users");
        
        List<User> allUsers = (List<User>)crud.getAll(User.class);
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
        User user = (User)crud.find(id,User.class);
        user.setFirstName(firstname);
        crud.edit(user);
        
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
        
        crud.create(mail);        
        
    }
    
    
    
    @Path("showMails")
    public void showMails(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("Test");
        
        List<Mail> allMails = (List<Mail>)crud.getAll(Mail.class);
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
                Logger.getLogger(TestAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Path("generateMail")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void generateMail(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        Long mailId = Long.parseLong(request.getParameter("mail_id"));
        Long userId = Long.parseLong(request.getParameter("user_id"));
        
        User user = (User)crud.find(userId,User.class);
        Mail mail = (Mail)crud.find(mailId,Mail.class);
        
        QueuedMail queuedMail = mailQueue.createQueuedMail(user,mail,null);
        
        GeneratedMail generatedMail = generatedMails.generateMail(queuedMail);

        /*
        queuedMail.setGeneratedMail(generatedMail);
        mailQueue.edit(queuedMail);
        */
        
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
            Mail mail = (Mail)crud.find(mailId,Mail.class);
            
            java.util.Date time = null;
            String timeString = request.getParameter("send_time");
            if (timeString != null) {
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = fm.parse(timeString);
            }

            mailManager.scheduleMail(mail, real, time, delay);

            writer.println("scheduled");

        } catch (ParseException|NumberFormatException ex) {
            Logger.getLogger(TestAdminController.class.getName()).log(Level.SEVERE, null, ex);
            writer.println("parse failed");
        }
        
    }
    
    @Path("sendMail")
    public void sendMail(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        Long generatedMailId = Long.parseLong(request.getParameter("generated_id"));
        GeneratedMail generatedMail = (GeneratedMail)crud.find(generatedMailId,GeneratedMail.class);
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
            crud.create(series);
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
            Series series = (Series)crud.find(seriesId, Series.class);
            Mail mail = (Mail)crud.find(mailId, Mail.class);
            SeriesItem seriesItem = new SeriesItem();
            seriesItem.setMail(mail);
            seriesItem.setSeries(series);
            seriesItem.setSendDelay(sendDelay);
            crud.create(seriesItem);
            
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
            Series series = (Series)crud.find(seriesId, Series.class);
            User user = (User)crud.find(userId, User.class);
            SeriesSubscription seriesSubscription = new SeriesSubscription();
            seriesSubscription.setSeries(series);
            seriesSubscription.setUser(user);
            seriesSubscription.setSubscribeTime(new java.util.Date());
            crud.create(seriesSubscription);
            
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
            Series series = (Series)crud.find(seriesId, Series.class);
            
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
        
        mailSeries.processAllSeriesAsync();
            
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
        crud.create(campaign);
        
        writer.println("Added, id: "+campaign.getId());
        
    }
    
    @Path("addMailToCampaign")
    public void addMailToCampaign(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        Long campaignId = Long.parseLong(request.getParameter("campaign_id"));
        Long mailId = Long.parseLong(request.getParameter("mail_id"));
        
        Campaign campaign = (Campaign)crud.find(campaignId, Campaign.class);
        Mail mail = (Mail)crud.find(mailId, Mail.class);
        
        Set<Mail> mails = campaign.getMails();
        mails.add(mail);
        crud.edit(campaign);

        writer.println("Added");
        
    }
    
    @Path("removeMailFromCampaign")
    public void removeMailFromCampaign(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        Long campaignId = Long.parseLong(request.getParameter("campaign_id"));
        Long mailId = Long.parseLong(request.getParameter("mail_id"));
        
        Campaign campaign = (Campaign)crud.find(campaignId, Campaign.class);
        Mail mail = (Mail)crud.find(mailId, Mail.class);
        
        Set<Mail> mails = campaign.getMails();
        mails.remove(mail);
        crud.edit(campaign);

        writer.println("Removed");
        
    }
    
    @Path("unsubscribeFromCampaign")
    public void unsubscribeFromCampaign(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        try {

            User user = userManager.getByEmail(request.getParameter("email"));
            Campaign campaign = campaigns.getByName(request.getParameter("campaign_name"));
            campaign.getUnsubscribedUsers().add(user);
            crud.edit(campaign);
            writer.println("Successfully unsubscribed from campaign "+campaign.getLongName()); 

        } catch(Exception e) {
            writer.println("Unsubscribe unsuccessful");
        }
    }
}
