/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.QueuedMail;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ferenci84
 */
@Stateless
public class GeneratedMails extends Crud<GeneratedMail> {
    
    @Inject MailQueue mailQueue;
    
    public GeneratedMails()
    {
        super(GeneratedMail.class);
    }
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    protected EntityManager getEntityManager() {
        return em;
    }

    public GeneratedMail generateMail(QueuedMail queuedMail)
    {
        GeneratedMail generatedMail = new GeneratedMail();
        
        generatedMail.setFromEmail(queuedMail.getMail().getFrom());
        generatedMail.setToEmail(queuedMail.getUser().getEmail());
        generatedMail.setSubject(queuedMail.getMail().getSubject());
        generatedMail.setBody(queuedMail.getMail().getBody_text());
        this.create(generatedMail);
        
        queuedMail.setGeneratedMail(generatedMail);
        mailQueue.edit(queuedMail);

        return generatedMail;
                
    }
    
}
