package dps.simplemailing.mailqueue;

import dps.simplemailing.back.Crud;
import dps.simplemailing.entities.*;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

@Stateless
public class MailQueue {

    @Inject
    MailSending mailSending;
    @Inject
    MailGenerator generatedMails;
    @Inject
    MailQueueStatus queueStatus;
    @Inject
    MailQueue mailQueue;

    @Inject
    Crud crud;

    public QueuedMail createQueuedMail(User user, Mail mail, java.util.Date scheduledTime)
    {
        QueuedMail queuedMail = new QueuedMail();

        queuedMail.setUser(user);
        queuedMail.setMail(mail);
        queuedMail.setScheduledTime(scheduledTime);
        queuedMail.setStatus(QueuedMail.Status.unsent);
        crud.create(queuedMail);

        return queuedMail;
    }

    public List<QueuedMail> getQueueToSend() {
        TypedQuery<QueuedMail> query = crud.getEntityManager().createQuery("SELECT m FROM QueuedMail m WHERE m.status = :status AND (m.scheduledTime is null OR m.scheduledTime <= :now)",QueuedMail.class);
        query.setParameter("status", QueuedMail.Status.unsent);
        query.setParameter("now", new java.util.Date());
        return query.getResultList();
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void processQueue()
    {
        if (queueStatus.getStarted()) {
            //System.out.println("queue is already started");
            return;
        }
        try {
            queueStatus.setStarted(true);
            List<QueuedMail> queueToSend = getQueueToSend();

            if (queueToSend.size() != 0) {
                System.out.println("Queue to send: "+queueToSend.size());
                mailQueue.generateMails(queueToSend);
                mailQueue.sendMails(queueToSend);
                mailQueue.cleanupQueue();
            }

        } finally {
            queueStatus.setStarted(false);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void generateMails(List<QueuedMail> queueToSend)
    {
        for (QueuedMail queuedMail: queueToSend) {
            System.out.println("Generating "+queuedMail);
            generatedMails.generateMail(queuedMail);
            queueStatus.setGenerated(queueStatus.getGenerated()+1);
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void sendMails(List<QueuedMail> queueToSend)
    {
        for (QueuedMail queuedMail: queueToSend) {
            mailQueue.sendMail(queuedMail);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {}
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void sendMail(QueuedMail queuedMail)
    {
        System.out.println("Checking "+queuedMail);

        queuedMail = crud.getEntityManager().merge(queuedMail);
        Mail mail = queuedMail.getMail();

        Boolean unsubscribed = false;
        Set<Campaign> campaigns = mail.getCampaigns();
        for (Campaign campaign: campaigns) {
            if (campaign.getUnsubscribedUsers().contains(queuedMail.getUser())) {
                unsubscribed = true;
            }
        }

        if (!unsubscribed) {
            GeneratedMail generatedMail = queuedMail.getGeneratedMail();
            if (generatedMail != null) {
                System.out.println("sending "+generatedMail);
                if (mailSending.sendMail(generatedMail)) {
                    queuedMail.setStatus(QueuedMail.Status.sent);
                    crud.edit(queuedMail);
                    queueStatus.setSent(queueStatus.getSent()+1);
                } else {
                    queuedMail.setStatus(QueuedMail.Status.fail);
                    crud.edit(queuedMail);
                    queueStatus.setFailed(queueStatus.getFailed()+1);
                }
            }
        } else {

            queuedMail.setStatus(QueuedMail.Status.fail);
            crud.edit(queuedMail);
            queueStatus.setFailed(queueStatus.getFailed()+1);

            System.out.println("User unsubscribed from campaign");
        }
    }

    public void cleanupQueue()
    {
        //System.out.println("cleaning up");
        TypedQuery<QueuedMail> query = crud.getEntityManager().createQuery("SELECT m FROM QueuedMail m WHERE (m.status <> :status1) AND (m.generatedMail IS NOT NULL)",QueuedMail.class);
        query.setParameter("status1", QueuedMail.Status.unsent);
        List<QueuedMail> queuedMails = query.getResultList();

        if (queuedMails.size() != 0) {
            System.out.println("queuedMails to clean up: "+queuedMails.size());
            for (QueuedMail queuedMail: queuedMails) {
                GeneratedMail generatedMail = queuedMail.getGeneratedMail();
                if (generatedMail != null) {
                    crud.remove(generatedMail);
                }
                queuedMail.setGeneratedMail(null);
                crud.edit(queuedMail);
            }
        }

    }




}