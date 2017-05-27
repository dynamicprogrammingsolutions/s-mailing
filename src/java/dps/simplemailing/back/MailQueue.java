/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MailQueue extends Crud<QueuedMail>{
    
    public MailQueue()
    {
        super(QueuedMail.class);
    }
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    protected EntityManager getEntityManager() {
        return em;
    }

    public QueuedMail createQueuedMail(User user, Mail mail, java.sql.Time scheduledTime)
    {
        QueuedMail queuedMail = new QueuedMail();
        
        queuedMail.setUser(user);
        queuedMail.setMail(mail);
        queuedMail.setScheduledTime(scheduledTime);
        queuedMail.setStatus(QueuedMail.Status.unsent);
        this.create(queuedMail);
        
        return queuedMail;
    }
    
    
    
}
