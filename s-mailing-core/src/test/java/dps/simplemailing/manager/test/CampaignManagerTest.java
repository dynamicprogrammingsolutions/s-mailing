package dps.simplemailing.manager.test;

import dps.simplemailing.entities.*;
import dps.simplemailing.manage.CampaignManager;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.manage.ManagerBase;
import dps.simplemailing.manage.UserManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@ApplicationScoped
public class CampaignManagerTest extends ManagerTestBase<Campaign> {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addPackage("dps.simplemailing.manager.test")
                .addPackage("dps.simplemailing.entities")
                .addPackage("dps.crud")
                .addPackage("dps.simplemailing.crud")
                .addPackage("dps.simplemailing.manage")
                .addPackage("dps.simplemailing.mailqueue")
                .addPackage("dps.logging")
                .addPackage("dps.commons.reflect")
                .addPackage("dps.commons.configuration")
                .addAsResource("s-mailing.conf.xml", "META-INF/s-mailing.conf.xml")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    protected CampaignManager manager;

    @Inject
    protected MailManager mailManager;

    @Inject
    protected MailManagerTest mailManagerTest;

    @Inject UserManagerTest userManagerTest;

    @Inject UserManager userManager;

    @Override
    protected ManagerBase<Campaign,Long> getManager() {
        return manager;
    }

    @Override
    protected Campaign getTestData(Campaign campaign) {
        campaign.setName("Test");
        campaign.setLongName("Test Campaign");
        return campaign;
    }

    @Override
    protected void assertTestData(Campaign campaign) {
        assertEquals(campaign.getName(),"Test");
        assertEquals(campaign.getLongName(), "Test Campaign");
    }

    @Override
    protected void setInvalidData(Campaign campaign) {
        campaign.setName(null);
    }

    @Override
    protected void setModifiedData(Campaign campaign) {
        campaign.setName("TestModified");
    }

    @Override
    protected void assertModifiedData(Campaign campaign) {
        assertEquals(campaign.getName(), "TestModified");
    }

    @Test
    public void testAddMailRemoveMail()
    {
        Long cnt = manager.count();
        Long mailCnt = mailManager.count();

        Campaign campaign = createTestData();
        Long campaignId = campaign.getId();

        Mail mail = mailManagerTest.createTestData();
        Long mailId = mail.getId();

        Set<Mail> campaignMails = manager.reload(campaign,Campaign_.mails).getMails();
        assertFalse(campaignMails.contains(mail));

        Set<Campaign> mailCampaigns = mailManager.reload(mail,Mail_.campaigns).getCampaigns();
        assertFalse(mailCampaigns.contains(campaign));

        manager.addMail(campaignId,mailId);

        mail = mailManager.reload(mail, Mail_.campaigns);
        campaign = manager.reload(campaign, Campaign_.mails);

        assertTrue(mail.getCampaigns().contains(campaign));
        assertTrue(campaign.getMails().contains(mail));

        manager.removeMail(campaignId,mailId);

        mail = mailManager.reload(mail, Mail_.campaigns);
        campaign = manager.reload(campaign, Campaign_.mails);

        assertFalse(mail.getCampaigns().contains(campaign));
        assertFalse(campaign.getMails().contains(mail));

        mailManagerTest.removeTestData(mailId);
        this.removeTestData(campaignId);

        assertEquals(mailCnt, mailManager.count());
        assertEquals(cnt, manager.count());
    }

    @Test
    public void unsibscribeUserFromCampaign()
    {
        Long cnt = manager.count();
        Campaign campaign = createTestData();
        Long campaignId = campaign.getId();

        Long userCnt = userManager.count();

        User user = userManagerTest.createTestData();
        Long userId = user.getId();

        manager.unsubscribeUser(campaignId,userId);

        Set<User> unsubscribedUsers = manager.reload(campaign,Campaign_.unsubscribedUsers).getUnsubscribedUsers();
        assertTrue(unsubscribedUsers.contains(user));

        this.removeTestData(campaignId);
        userManagerTest.removeTestData(userId);
        assertEquals(cnt, manager.count());
        assertEquals(userCnt, userManager.count());
    }


}