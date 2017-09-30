package dps.simplemailing.manage;

import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CampaignManager extends ManagerBase<Campaign,Long> {

    @Inject
    MailManager mailManager;

    @Inject UserManager userManager;

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
        this.modify(campaign.getId(),campaign);
    }

}
