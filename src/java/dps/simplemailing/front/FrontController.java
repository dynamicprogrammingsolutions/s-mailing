/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.Controller;
import dps.servletcontroller.Path;
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
public class FrontController extends Controller {
    
    @Inject Users userManager;
    @Inject Mails mailManager;
    @Inject MailQueue mailQueue;
    @Inject GeneratedMails generatedMails;
    @Inject MailSending mailSending;
    @Inject MailQueueStatus mailQueueStatus;
    
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
        User user = userManager.find(id);
        user.setFirstName(firstname);
        userManager.edit(user);
        
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
        
        /*List<QueuedMail> queueToSend = mailQueue.getQueueToSend();
        for (QueuedMail queuedMail: queueToSend) {
            writer.println("generating "+queuedMail);
            System.out.println("sending "+queuedMail);
            generatedMails.generateMail(queuedMail);
        }
        
        for (QueuedMail queuedMail: queueToSend) {
            GeneratedMail generatedMail = queuedMail.getGeneratedMail();
            if (generatedMail != null) {
                writer.println("sending "+generatedMail);
                System.out.println("sending "+generatedMail);
                if (mailSending.sendMail(generatedMail)) {
                    queuedMail.setStatus(QueuedMail.Status.sent);
                    mailQueue.edit(queuedMail);
                } else {
                    queuedMail.setStatus(QueuedMail.Status.fail);
                    mailQueue.edit(queuedMail);
                }
            }
        }*/
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
                Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        User user = userManager.find(userId);
        Mail mail = mailManager.find(mailId);
        
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
            Mail mail = mailManager.find(mailId);
            
            java.util.Date time = null;
            String timeString = request.getParameter("send_time");
            if (timeString != null) {
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = fm.parse(timeString);
            }

            mailManager.scheduleMail(mail, time);

            writer.println("scheduled");

        } catch (ParseException|NumberFormatException ex) {
            Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
            writer.println("parse failed");
        }
        
    }
    
    @Path("testMail")
    public void testMail(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        
        try {

            Long mailId = Long.parseLong(request.getParameter("mail_id"));
            Mail mail = mailManager.find(mailId);

            mailManager.testMail(mail);

            writer.println("scheduled");

        } catch (NumberFormatException ex) {
            Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
            writer.println("parse failed");
        }
        
    }
    
    @Path("sendMail")
    public void sendMail(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        Long generatedMailId = Long.parseLong(request.getParameter("generated_id"));
        GeneratedMail generatedMail = generatedMails.find(generatedMailId);
        mailSending.sendMail(generatedMail);
    }

    @Path("unsubscribe")
    public void unsubscribe(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String email = request.getParameter("email");
            System.out.println("unsubscribing id "+id+" email "+email);
            User user = userManager.find(id);
            if (user.getEmail().equals(email)) {
                userManager.unsubscribe(user);
                writer.println("Successfully unsubscribed");
            } else {
                throw new Exception();
            }
        } catch(Exception e) {
            writer.println("Unsubscribe unsuccessful");
        }
        
    }
    
}
