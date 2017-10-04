package dps.simplemailing.manage;

import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;
import dps.simplemailing.mailqueue.MailQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class MailManager extends ManagerBase<Mail,Long> {

    // TODO: replace Users to UserManager
    @Inject
    UserManager users;

    @Inject
    MailQueue queue;

    //TODO: fetch query
    @Transactional(TxType.REQUIRED)
    public Set<Campaign> getCampaigns(Long mailId)
    {
        Mail entity = em.find(Mail.class,mailId);
        if (entity == null) throw new EntityNotFoundException();
        Set<Campaign> campaigns = entity.getCampaigns();
        for (Campaign campaign: campaigns) {
            campaign.getId();
        }
        return campaigns;
    }
/*
    @Transactional(TxType.REQUIRED)
    public void addMailToCampaign(Long id, Long campaignId)
    {
        Mail mail = em.find(Mail.class,id);
        if (mail == null) throw new IllegalArgumentException("Mail or Campaign not found");
        Campaign campaign = em.find(Campaign.class,campaignId);
        if (campaign == null) throw new IllegalArgumentException("Mail or Campaign not found");
        campaign.getMails().add(mail);
        em.merge(campaign);
    }
*/
    @Transactional(Transactional.TxType.REQUIRED)
    public void scheduleMail(Mail mail, Boolean real, java.util.Date time, int msDelay)
    {
        Calendar cal = Calendar.getInstance();
        if (time == null) {
            cal.setTime(new java.util.Date());
        } else {
            cal.setTime(time);
        }

        List<User> allUsers;
        if (real) {
            allUsers = users.getActive();
        } else {
            allUsers = users.getTest();
        }

        for (User user: allUsers) {
            cal.add(Calendar.MILLISECOND, msDelay);
            queue.createQueuedMail(user, mail, cal.getTime());
        }
    }

}
