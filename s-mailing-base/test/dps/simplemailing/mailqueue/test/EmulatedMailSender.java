package dps.simplemailing.mailqueue.test;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.mailqueue.MailSender;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class EmulatedMailSender implements MailSender {

    private Set<GeneratedMail> sentMails = new HashSet<>();

    public void resetSentMails()
    {
        sentMails.clear();
    }

    public Boolean isSent(GeneratedMail generatedMail)
    {
        System.out.println("checking if sent: "+generatedMail);
        return sentMails.contains(generatedMail);
    }

    public Set<GeneratedMail> getSentMails()
    {
        return sentMails;
    }

    @Override
    public Boolean sendMail(GeneratedMail generatedMail) {
        System.out.println("Emulated sending: "+generatedMail);
        sentMails.add(generatedMail);
        return true;
    }
}
