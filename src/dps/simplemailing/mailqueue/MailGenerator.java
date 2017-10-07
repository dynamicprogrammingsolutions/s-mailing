package dps.simplemailing.mailqueue;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.entities.User;
import dps.simplemailing.manage.UseEntityManager;
import dps.simplemailing.manage.UserManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Map;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
public class MailGenerator extends UseEntityManager {
    
    @Inject
    UserManager users;

    public GeneratedMail getById(Long id)
    {
        return em.find(GeneratedMail.class,id);
    }
    
    @Transactional(Transactional.TxType.REQUIRED)
    public GeneratedMail generateMail(QueuedMail queuedMail)
    {
        GeneratedMail generatedMail = new GeneratedMail();
        
        generatedMail.setFromEmail(queuedMail.getMail().getFrom());
        generatedMail.setToEmail(queuedMail.getUser().getEmail());
        generatedMail.setSubject(queuedMail.getMail().getSubject());
        
        String body_text = queuedMail.getMail().getBody_text();
        body_text = processPlaceholders(queuedMail.getUser(), body_text);
        
        generatedMail.setBody(body_text);
        //crud.create(generatedMail);
        em.persist(generatedMail);
        
        queuedMail.setGeneratedMail(generatedMail);
        em.merge(queuedMail);

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
