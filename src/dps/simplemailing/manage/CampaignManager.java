package dps.simplemailing.manage;

import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CampaignManager extends ManagerBase<Campaign,Long> {

    @Inject
    MailManager mailManager;

    @Inject UserManager userManager;

    //TODO: Fetch query
    @Transactional(Transactional.TxType.REQUIRED)
    public Set<Mail> getMails(Long campaignId)
    {
        Campaign entity = em.find(Campaign.class,campaignId);
        if (entity == null) throw new EntityNotFoundException();
        Set<Mail> mails = entity.getMails();
        for (Mail mail: mails) {
            mail.getId();
        }
        return mails;
    }

    public Campaign getByName(String name)
    {
        Query query = em.createQuery("SELECT u FROM Campaign u WHERE u.name = :name");
        query.setParameter("name", name);
        List<Campaign> campaigns = query.getResultList();
        if (campaigns.isEmpty()) return null;
        else return campaigns.get(0);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void addMail(Long campaignId, Long mailId)
    {
        Campaign campaign = this.getById(campaignId);
        Mail mail = mailManager.getById(mailId);
        Set<Mail> mails = campaign.getMails();
        mails.add(mail);
        this.modify(campaign);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void removeMail(Long campaignId, Long mailId)
    {
        Campaign campaign = this.getById(campaignId);
        Mail mail = mailManager.getById(mailId);
        Set<Mail> mails = campaign.getMails();
        mails.remove(mail);
        this.modify(campaign);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void unsubscribeUser(String campaignName, String userEmail)
    {
        User user = userManager.getByEmail(userEmail);
        Campaign campaign = this.getByName(campaignName);
        campaign.getUnsubscribedUsers().add(user);
        em.merge(campaign);
        //this.modify(campaign.getId(),campaign);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void unsubscribeUser(Long campaignId, Long userId)
    {
        User user = userManager.getById(userId);
        Campaign campaign = this.getById(campaignId);
        campaign.getUnsubscribedUsers().add(user);
        em.merge(campaign);
        //this.modify(campaign.getId(),campaign);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Set<User> getUnsubscribedUsers(Long campaignId)
    {
        Campaign campaign = this.getById(campaignId);
        Set<User> users = campaign.getUnsubscribedUsers();
        for (User user: users)
        {
            user.getId();
        }
        return users;
    }

}
