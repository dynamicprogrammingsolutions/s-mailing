package dps.simplemailing.mailqueue;

import dps.simplemailing.entities.GeneratedMail;

public interface MailSender {
    public Boolean sendMail(GeneratedMail generatedMail);
}
