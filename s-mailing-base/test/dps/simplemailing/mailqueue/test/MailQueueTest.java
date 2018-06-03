package dps.simplemailing.mailqueue.test;

import dps.simplemailing.entities.*;
import dps.simplemailing.mailqueue.*;
import dps.simplemailing.manage.GeneratedMailManager;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.manager.test.MailManagerTest;
import dps.simplemailing.manager.test.UserManagerTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@RunWith(Arquillian.class)
public class MailQueueTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addPackage("dps.simplemailing.manager.test")
                .addPackage("dps.simplemailing.mailqueue.test")
                .addPackage("dps.simplemailing.entities")
                .addPackage("dps.crud")
                .addPackage("dps.simplemailing.crud")
                .addPackage("dps.simplemailing.manage")
                .addClass(MailSender.class)
                .addClass(GeneratedMailManager.class)
                .addClass(MailQueue.class)
                .addClass(MailQueueStatus.class)
                .addClass(MailGenerator.class)
                .addClass(DefaultMailGenerator.class)
                .addClass(MailSettings.class)
                .addPackage("dps.reflect")
                .addPackage("dps.logging")
                .addAsResource("test-smtp-conf.json", "smtp-conf.json")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    MailManagerTest mailManagerTest;

    @Inject
    UserManagerTest userManagerTest;

    @Inject
    MailManager mailManager;

    @Inject
    MailQueue mailQueue;

    @Inject
    EmulatedMailSender emulatedMailSender;


    @Test
    public void testSendingElements()
    {
        Mail mail = mailManagerTest.createTestData();
        User user = userManagerTest.createTestData();

        long now = new Date().getTime();

        mailQueue.createQueuedMail(user,mail,new Date(now));
        List<QueuedMail> queueToSend = mailQueue.getQueueToSend();
        Assert.assertEquals(1,queueToSend.size());

        mailQueue.generateMails(queueToSend);
        QueuedMail queuedMail = queueToSend.get(0);

        queuedMail = mailQueue.reload(queuedMail, QueuedMail_.generatedMail);
        GeneratedMail generatedMail = queuedMail.getGeneratedMail();
        Assert.assertNotNull(generatedMail);
        System.out.println("generated mail: "+generatedMail);

        mailQueue.sendMails(queueToSend);

        Assert.assertTrue(emulatedMailSender.isSent(generatedMail));
        Assert.assertEquals(0,mailQueue.getQueueToSend().size());

        mailQueue.cleanupQueue();
        mailQueue.removeAllUnsent();

        mailManagerTest.removeTestData(mail.getId());
        userManagerTest.removeTestData(user.getId());

    }

    //TODO: emulating time and test future scheduled sending
    @Test
    public void testProcessQueueSendingNow()
    {
        Mail mail = mailManagerTest.createTestData();
        User user = userManagerTest.createTestData();

        long now = new Date().getTime();

        emulatedMailSender.resetSentMails();

        mailManager.scheduleMail(mail,true,new Date(now),0);
        Assert.assertEquals(0,emulatedMailSender.getSentMails().size());
        mailQueue.processQueue();
        Assert.assertEquals(1,emulatedMailSender.getSentMails().size());

        mailManagerTest.removeTestData(mail.getId());
        userManagerTest.removeTestData(user.getId());

    }

    @Inject
    MailSettings mailSettings;

    @Test
    public void testLoadSettings()
    {
        Assert.assertNotNull(mailSettings.getHost());
        Assert.assertNotNull(mailSettings.getPassword());
        Assert.assertNotNull(mailSettings.getPort());
        Assert.assertNotNull(mailSettings.getUsername());
    }

}
