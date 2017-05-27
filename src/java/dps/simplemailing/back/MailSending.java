/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.GeneratedMail;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ferenci84
 */
@Singleton
public class MailSending {
    
    final private String host = "email-smtp.us-west-2.amazonaws.com";
    final private String port = "587";
    final private String username = "AKIAJ5FEM3AFFMKMCC2A";
    final private String password = "AjJGp8+C/yC8NuFYwhR053/JV8abWaRWI7xJ7LjFcvz4";
    
    Properties prop;
    
    @PostConstruct
    public void postConstruct()
    {
        prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
    }
    
    public void sendMail(GeneratedMail generatedMail)
    {
        System.out.println("Sending id " + generatedMail.getId() + " to " + generatedMail.getToEmail());
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(generatedMail.getFromEmail()));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(generatedMail.getToEmail()));
            message.setSubject(generatedMail.getSubject());
            message.setText(generatedMail.getBody());
            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(MailSending.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MailSending.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
