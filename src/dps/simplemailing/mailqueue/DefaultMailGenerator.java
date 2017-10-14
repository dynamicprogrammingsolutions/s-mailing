package dps.simplemailing.mailqueue;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;
import dps.simplemailing.manage.UserManager;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Map;

@Dependent
public class DefaultMailGenerator implements MailGenerator {

    @Inject
    UserManager users;

    @Override
    public GeneratedMail generateMail(GeneratedMail generatedMail, Mail mail, User user) {


        generatedMail.setFromEmail(mail.getFrom());
        generatedMail.setToEmail(user.getEmail());
        generatedMail.setSubject(mail.getSubject());

        String body_text = mail.getBody_text();
        body_text = processPlaceholders(user, body_text);

        generatedMail.setBody(body_text);

        return generatedMail;

    }

    @SuppressWarnings("Annotator")
    private String processPlaceholders(User user, String text)
    {
        Map<String,String> placeholders = users.getPlaceholders(user);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            text = text.replaceAll("\\{"+key+"\\}", value);
        }
        return text;
    }

}
