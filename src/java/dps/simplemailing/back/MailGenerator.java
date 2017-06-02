/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.entities.User;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ferenci84
 */
@Stateless
public class MailGenerator {
    
    @Inject Crud crud;    
    @Inject Users users;
    
    public GeneratedMail generateMail(QueuedMail queuedMail)
    {
        GeneratedMail generatedMail = new GeneratedMail();
        
        generatedMail.setFromEmail(queuedMail.getMail().getFrom());
        generatedMail.setToEmail(queuedMail.getUser().getEmail());
        generatedMail.setSubject(queuedMail.getMail().getSubject());
        
        String body_text = queuedMail.getMail().getBody_text();
        body_text = processPlaceholders(queuedMail.getUser(), body_text);
        
        generatedMail.setBody(body_text);
        crud.create(generatedMail);
        
        queuedMail.setGeneratedMail(generatedMail);
        crud.edit(queuedMail);

        return generatedMail;
                
    }
    
    public String processPlaceholders(User user, String text)
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
