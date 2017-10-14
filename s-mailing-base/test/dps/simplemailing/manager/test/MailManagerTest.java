package dps.simplemailing.manager.test;

import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Mail_;
import dps.simplemailing.manage.CampaignManager;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.entities.Mail;

import static org.junit.Assert.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import dps.simplemailing.manage.ManagerBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

@RunWith(Arquillian.class)
@ApplicationScoped
public class MailManagerTest extends ManagerTestBase<Mail> {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addPackage("dps.simplemailing.manager.test")
                .addPackage("dps.simplemailing.entities")
                .addPackage("dps.crud")
                .addPackage("dps.simplemailing.crud")
                .addPackage("dps.simplemailing.manage")
                .addPackage("dps.simplemailing.mailqueue")
                .addPackage("dps.reflect")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    MailManager manager;

    @Inject
    CampaignManagerTest campaignManagerTest;

    @Inject
    CampaignManager campaignManager;

    @Override
    protected ManagerBase<Mail,Long> getManager() {
        return manager;
    }

    @Override
    protected Mail getTestData(Mail mail) {
        mail.setName("Test Mail 1");
        mail.setSubject("Test Mail 1 Subject");
        mail.setBody_text("Test Mail 1 Body");
        mail.setFrom("from@test.com"); 
        return mail;
    }

    @Override
    protected void assertTestData(Mail mail) {
        assertEquals(mail.getName(), "Test Mail 1");
    }

    @Override
    protected void setInvalidData(Mail mail) {
        mail.setName(null);
    }

    @Override
    protected void setModifiedData(Mail mail) {
        mail.setName("Test Mail 1 modified");
    }

    @Override
    protected void assertModifiedData(Mail mail) {
        assertEquals(mail.getName(), "Test Mail 1 modified");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCampaigns()
    {
        Long cnt = getManager().count();
        Long campaignCnt = campaignManager.count();

        Mail mail = this.createTestData();
        Long mailId = mail.getId();
        Campaign campaign = campaignManagerTest.createTestData();
        Long campaignId = campaign.getId();

        Set<Campaign> mailCampaigns = manager.reload(mail, Mail_.campaigns).getCampaigns();
        assertFalse(mailCampaigns.contains(campaign));

        campaignManager.addMail(campaignId,mailId);

        mail = manager.getById(mailId);
        campaign = campaignManager.getById(campaignId);

        mailCampaigns = manager.reload(mail,Mail_.campaigns).getCampaigns();
        assertTrue(mailCampaigns.contains(campaign));

        campaignManagerTest.removeTestData(campaign.getId());
        this.removeTestData(mail.getId());

        assertEquals(campaignCnt, campaignManager.count());
        assertEquals(cnt, getManager().count());
    }

}