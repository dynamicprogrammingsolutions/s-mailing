/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.Controller;
import dps.servletcontroller.Path;
import dps.simplemailing.back.Crud;
import dps.simplemailing.back.GeneratedMails;
import dps.simplemailing.back.MailQueue;
import dps.simplemailing.back.MailQueueStatus;
import dps.simplemailing.back.MailSending;
import dps.simplemailing.back.Mails;
import dps.simplemailing.back.Users;
import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
@Path("/(.*)")
public class AdminController extends Controller {
    
    @Inject Users userManager;
    @Inject Mails mailManager;
    @Inject MailQueue mailQueue;
    @Inject GeneratedMails generatedMails;
    @Inject MailSending mailSending;
    @Inject MailQueueStatus mailQueueStatus;
    
    @Inject Crud crud;
    
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
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Path("generateMail")
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
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
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
    
}