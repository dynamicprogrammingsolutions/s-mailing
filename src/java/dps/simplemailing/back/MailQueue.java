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
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

@Stateless
public class MailQueue extends Crud<QueuedMail>{
    
    @Inject MailSending mailSending;
    @Inject GeneratedMails generatedMails;
    @Inject MailQueueStatus queueStatus;
    
    public MailQueue()
    {
        super(QueuedMail.class);
    }
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    protected EntityManager getEntityManager() {
        return em;
    }

    public QueuedMail createQueuedMail(User user, Mail mail, java.util.Date scheduledTime)
    {
        QueuedMail queuedMail = new QueuedMail();
        
        queuedMail.setUser(user);
        queuedMail.setMail(mail);
        queuedMail.setScheduledTime(scheduledTime);
        queuedMail.setStatus(QueuedMail.Status.unsent);
        this.create(queuedMail);
        
        return queuedMail;
    }
    
    public List<QueuedMail> getQueueToSend() {
        Query query = getEntityManager().createQuery("SELECT m FROM QueuedMail m WHERE m.status = :status AND (m.scheduledTime = null OR m.scheduledTime <= :now)");
        query.setParameter("status", QueuedMail.Status.unsent);
        query.setParameter("now", new java.util.Date());
        return query.getResultList();
    }
    
    @Asynchronous
    public void processQueue()
    {
        if (queueStatus.getStarted()) {
            System.out.println("queue is already started");
            return;
        }
        try {
            queueStatus.setStarted(true);
            List<QueuedMail> queueToSend = getQueueToSend();
            generateMails(queueToSend);
            sendMails(queueToSend);
        } finally {
            queueStatus.setStarted(false);
        }
    }
    
    public void generateMails(List<QueuedMail> queueToSend)
    {
        for (QueuedMail queuedMail: queueToSend) {
            System.out.println("sending "+queuedMail);
            generatedMails.generateMail(queuedMail);
            queueStatus.setGenerated(queueStatus.getGenerated()+1);
        }
    }
    
    public void sendMails(List<QueuedMail> queueToSend)
    {
        for (QueuedMail queuedMail: queueToSend) {
            GeneratedMail generatedMail = queuedMail.getGeneratedMail();
            if (generatedMail != null) {
                System.out.println("sending "+generatedMail);
                if (mailSending.sendMail(generatedMail)) {
                    queuedMail.setStatus(QueuedMail.Status.sent);
                    edit(queuedMail);
                    queueStatus.setSent(queueStatus.getSent()+1);
                } else {
                    queuedMail.setStatus(QueuedMail.Status.fail);
                    edit(queuedMail);
                    queueStatus.setFailed(queueStatus.getFailed()+1);
                }
            }
        }
    }
    
    
    
    
}
