package dps.simplemailing.mailqueue;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;

public interface MailGenerator {
    GeneratedMail generateMail(GeneratedMail generatedMail, Mail mail, User user);
}
