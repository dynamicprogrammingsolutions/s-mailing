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
import dps.simplemailing.back.MailSending;
import dps.simplemailing.back.Mails;
import dps.simplemailing.back.Users;
import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
    
    @Path("sendMail")
    public void sendMail(HttpServletRequest request, HttpServletResponse response) throws  IOException
    {
        Long generatedMailId = Long.parseLong(request.getParameter("generated_id"));
        GeneratedMail generatedMail = generatedMails.find(generatedMailId);
        mailSending.sendMail(generatedMail);
    }

}
