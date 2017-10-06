package dps.simplemailing.manage;

import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CampaignManager extends ManagerBase<Campaign,Long> {

    @Inject
    MailManager mailManager;

    @Inject UserManager userManager;

    /*
    @Transactional(Transactional.TxType.REQUIRED)
    public Set<Mail> getMails(Long campaignId)
    {
        Campaign entity = this.getById(campaignId,"mails");
        if (entity == null) throw new EntityNotFoundException();
        Set<Mail> mails = entity.getMails();
        return mails;
    }
    */

    /*@Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void remove(Long id) throws IllegalArgumentException {
        Campaign entity = em.find(entityClass,id);
        entity.getMails().clear();
        entity.getUnsubscribedUsers().clear();
        em.merge(entity);
        em.remove(entity);
    }*/


    public Campaign getByName(String name)
    {
        TypedQuery<Campaign> query = em.createNamedQuery("Campaign.getByName",Campaign.class);
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
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void unsubscribeUser(Long campaignId, Long userId)
    {
        User user = userManager.getById(userId);
        Campaign campaign = this.getById(campaignId);
        campaign.getUnsubscribedUsers().add(user);
        em.merge(campaign);
    }

    /*
    @Transactional(Transactional.TxType.REQUIRED)
    public Set<User> getUnsubscribedUsers(Long campaignId)
    {
        Campaign campaign = this.getById(campaignId,"unsubscribedUsers");
        return campaign.getUnsubscribedUsers();
    }
    */

}
