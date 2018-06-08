package dps.simplemailing.mailqueue;

import dps.logging.HasLogger;
import dps.simplemailing.entities.GeneratedMail;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author ferenci84
 */
@SuppressWarnings("ALL")
@ApplicationScoped
public class MailSending implements MailSender, HasLogger {

    /*
    final private String host = "email-smtp.us-west-2.amazonaws.com";
    final private String port = "587";
    final private String username = "AKIAJ5FEM3AFFMKMCC2A";
    final private String password = "AjJGp8+C/yC8NuFYwhR053/JV8abWaRWI7xJ7LjFcvz4";
    */

    @Inject
    MailSettings mailSettings;

    Properties prop;
    Session session;
    
    @PostConstruct
    public void postConstruct()
    {
        prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", mailSettings.getHost());
        prop.put("mail.smtp.port", mailSettings.getPort());
        
        session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSettings.getUsername(), mailSettings.getPassword());
            }
        });
    }

    public Boolean sendMail(GeneratedMail generatedMail)
    {
        logInfo("Sending email " + generatedMail.getSubject() + " from " + generatedMail.getFromEmail() + " to " + generatedMail.getToEmail());

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(generatedMail.getFromEmail()));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(generatedMail.getToEmail()));
            message.setSubject(generatedMail.getSubject());
            message.setText(generatedMail.getBody());
            Transport.send(message);
            logInfo("Message Sent");
            return true;
        } catch (MessagingException ex) {
            logWarning(ex.getMessage());
            return false;
        }
        
    }
    
    
}
