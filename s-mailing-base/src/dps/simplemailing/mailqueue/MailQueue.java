package dps.simplemailing.mailqueue;

import dps.logging.HasLogger;
import dps.simplemailing.entities.*;
import dps.simplemailing.manage.GeneratedMailManager;
import dps.simplemailing.manage.ManagerBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class MailQueue extends ManagerBase<QueuedMail,Long> implements HasLogger {

    @Inject
    MailSender mailSending;
    @Inject
    GeneratedMailManager generatedMails;
    @Inject
    MailQueueStatus queueStatus;
    @Inject
    MailQueue mailQueue;

    @Transactional(Transactional.TxType.REQUIRED)
    public QueuedMail createQueuedMail(User user, Mail mail, Date scheduledTime)
    {
        user = em.getReference(User.class,user.getId());
        mail = em.getReference(Mail.class,mail.getId());

        QueuedMail queuedMail = new QueuedMail();

        queuedMail.setUser(user);
        queuedMail.setMail(mail);
        queuedMail.setScheduledTime(scheduledTime);
        queuedMail.setStatus(QueuedMail.Status.unsent);

        em.persist(queuedMail);

        return queuedMail;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void removeAllUnsent()
    {
        Query query = em.createQuery("DELETE FROM QueuedMail m WHERE m.status = :status");
        query.setParameter("status", QueuedMail.Status.unsent);
        query.executeUpdate();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<QueuedMail> getQueueToSend() {
        TypedQuery<QueuedMail> query = em.createNamedQuery("QueuedMail.getQueue",QueuedMail.class);
        query.setParameter("status", QueuedMail.Status.unsent);
        //query.setParameter("now", getCurrentTime());
        return query.getResultList();
    }

    private Date currentTime = null;

    public Date getCurrentTime()
    {
        return currentTime!=null ? currentTime : new Date();
    }

    public void setCurrentTime(Date date)
    {
        currentTime = date;
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void processQueue()
    {
        //logInfo("processing queue");
        if (queueStatus.getStarted()) {
            logInfo("queue is already started");
            return;
        }
        try {
            queueStatus.setStarted(true);
            List<QueuedMail> queueToSend = mailQueue.getQueueToSend();

            if (queueToSend.size() != 0) {
                logFine("Queue to send: "+queueToSend.size());
                mailQueue.generateMails(queueToSend);
                mailQueue.sendMails(queueToSend);
                mailQueue.cleanupQueue();
            }

        } finally {
            queueStatus.setStarted(false);
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void generateMails(List<QueuedMail> queueToSend)
    {
        for (QueuedMail queuedMail: queueToSend) {
            logFiner("Generating "+queuedMail);
            queuedMail.setGeneratedMail(generatedMails.generateMail(queuedMail));
            mailQueue.modify(queuedMail);
            queueStatus.setGenerated(queueStatus.getGenerated()+1);
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void sendMails(List<QueuedMail> queueToSend)
    {
        for (QueuedMail queuedMail: queueToSend) {
            mailQueue.sendMail(queuedMail);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {}
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void sendMail(QueuedMail queuedMail)
    {
        queuedMail = em.merge(queuedMail);
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
                logFiner("sending "+generatedMail);
                if (mailSending.sendMail(generatedMail)) {
                    queuedMail.setStatus(QueuedMail.Status.sent);
                    em.merge(queuedMail);
                    queueStatus.setSent(queueStatus.getSent()+1);
                } else {
                    queuedMail.setStatus(QueuedMail.Status.fail);
                    em.merge(queuedMail);
                    queueStatus.setFailed(queueStatus.getFailed()+1);
                }
            }
        } else {

            queuedMail.setStatus(QueuedMail.Status.fail);
            em.merge(queuedMail);
            queueStatus.setFailed(queueStatus.getFailed()+1);

            logFiner("User unsubscribed from campaign");
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void cleanupQueue()
    {
        TypedQuery<QueuedMail> query = em.createNamedQuery("QueuedMail.getAllWithGenerated",QueuedMail.class);
        query.setParameter("status", QueuedMail.Status.unsent);
        List<QueuedMail> queuedMails = query.getResultList();

        if (queuedMails.size() != 0) {
            logFine("queuedMails to clean up: "+queuedMails.size());
            for (QueuedMail queuedMail: queuedMails) {
                GeneratedMail generatedMail = queuedMail.getGeneratedMail();
                if (generatedMail != null) {
                    em.remove(generatedMail);
                }
                queuedMail.setGeneratedMail(null);
                em.merge(queuedMail);
            }
        }

    }




}