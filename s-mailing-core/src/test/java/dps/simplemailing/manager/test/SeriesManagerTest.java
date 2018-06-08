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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@ApplicationScoped
public class SeriesManagerTest extends ManagerTestBase<Series> {

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

    @Override
    protected ManagerBase<Series,Long> getManager() {
        return manager;
    }

    @Override
    protected Series getTestData(Series series) {
        series.setName("Test");
        series.setDisplayName("Test Series");
        return series;
    }

    @Override
    protected void assertTestData(Series series) {
        assertEquals(series.getName(),"Test");
    }

    @Override
    protected void setInvalidData(Series series) {
        series.setName(null);
    }

    @Override
    protected void setModifiedData(Series series) {
        series.setName("TestModified");
    }

    @Override
    protected void assertModifiedData(Series series) {
        assertEquals(series.getName(), "TestModified");
    }

    @Test
    public void createSubscriptionGetSubscriptionTest()
    {
        Long cnt = getManager().count();
        Series series = this.createTestData();
        Long id = series.getId();

        Long userCnt = userManager.count();

        User user = userManagerTest.createTestData();
        Long userId = user.getId();

        SeriesSubscription subscription = new SeriesSubscription();
        subscription.setSubscribeTime(new Date());
        manager.createSubscription(id,userId,subscription);

        subscription = manager.getSubscription(user,series);
        assertNotNull(subscription);

        this.removeTestData(id);
        assertEquals(cnt, getManager().count());

        userManagerTest.removeTestData(userId);
        assertEquals(cnt, manager.count());

    }

    @Test
    public void createItemTest()
    {
        Long cnt = getManager().count();
        Series series = this.createTestData();
        Long id = series.getId();

        Long mailCnt = mailManager.count();
        Mail mail = mailManagerTest.createTestData();
        Long mailId = mail.getId();

        SeriesItem seriesItem = new SeriesItem();
        seriesItem.setSendDelay(0);
        manager.createItem(id,mailId,seriesItem);

        Boolean found = false;
        List<SeriesItem> items = manager.reload(series,Series_.seriesItems).getSeriesItems();
        for (SeriesItem item: items) {
            if (item.getMail().equals(mail)) found = true;
        }
        assertTrue(found);

        this.removeTestData(id);
        assertEquals(cnt, getManager().count());

        mailManagerTest.removeTestData(mailId);
        assertEquals(mailCnt,mailManager.count());
    }

    @Test
    public void testCreateSeriesMail()
    {
        Series series = this.createTestData();
        Mail mail = mailManagerTest.createTestData();
        SeriesItem seriesItem = new SeriesItem();
        seriesItem.setSendDelay(0);
        manager.createItem(series.getId(),mail.getId(),seriesItem);
        SeriesSubscription subscription = new SeriesSubscription();
        subscription.setSubscribeTime(new Date());
        User user = userManagerTest.createTestData();
        manager.createSubscription(series,user,subscription);

        SeriesMail seriesMail = manager.getSeriesMail(subscription,seriesItem);
        assertTrue(seriesMail == null);
        manager.createSeriesMail(subscription,seriesItem);
        seriesMail = manager.getSeriesMail(subscription,seriesItem);
        assertNotNull(seriesMail);

        manager.remove(series);
        mailManager.remove(mail);
        userManager.remove(user);
    }

}