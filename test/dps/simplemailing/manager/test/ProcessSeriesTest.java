package dps.simplemailing.manager.test;

import dps.simplemailing.entities.*;
import dps.simplemailing.mailqueue.MailQueue;
import dps.simplemailing.manage.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ProcessSeriesTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addPackage("dps.simplemailing.manager.test")
                .addPackage("dps.simplemailing.entities")
                .addPackage("dps.crud")
                .addPackage("dps.simplemailing.back")
                .addPackage("dps.simplemailing.manage")
                .addPackage("dps.simplemailing.mailqueue")
                .addPackage("dps.reflect")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    SeriesManagerTest seriesManagerTest;

    @Inject
    SeriesManager manager;

    @Inject
    UserManagerTest userManagerTest;

    @Inject
    UserManager userManager;

    @Inject
    MailManagerTest mailManagerTest;

    @Inject
    MailManager mailManager;

    @Inject
    MailQueue mailQueue;

    @Inject
    SeriesSubscriptionManager seriesSubscriptionManager;

    @Test
    public void testProcessSeries()
    {
        //Create test data
        Series series = seriesManagerTest.createTestData();
        Mail mail = mailManagerTest.createTestData();
        User user = userManagerTest.createTestData();

        // Create seriesItem
        SeriesItem seriesItem1 = new SeriesItem();
        seriesItem1.setSendDelay(1);
        manager.createItem(series.getId(),mail.getId(),seriesItem1);

        // Create seriesItem
        SeriesItem seriesItem2 = new SeriesItem();
        seriesItem2.setSendDelay(2);
        manager.createItem(series.getId(),mail.getId(),seriesItem2);

        // Create seriesSubscription
        SeriesSubscription subscription = new SeriesSubscription();

        subscription.setSubscribeTime(new Date());
        manager.createSubscription(series,user,subscription);

        subscription = seriesSubscriptionManager.reload(subscription);
        Long now = subscription.getSubscribeTime().getTime();

        manager.setUnit(Calendar.SECOND);

        Date simulatedCurrentTime = new Date(now+1000);
        manager.setCurrentTime(simulatedCurrentTime);
        mailQueue.setCurrentTime(simulatedCurrentTime);

        manager.processSeries(series);
        List<QueuedMail> queueToSend = mailQueue.getQueueToSend();
        System.out.println(queueToSend);
        assertEquals(1,queueToSend.size());

        simulatedCurrentTime = new Date(now+2000);
        manager.setCurrentTime(simulatedCurrentTime);
        mailQueue.setCurrentTime(simulatedCurrentTime);

        manager.processSeries(series);
        queueToSend = mailQueue.getQueueToSend();
        assertEquals(2,queueToSend.size());


        mailQueue.removeAllUnsent();
        manager.remove(series);
        mailManager.remove(mail);
        userManager.remove(user);


    }

}