package dps.simplemailing.manager.test;

import dps.simplemailing.entities.User;
import dps.simplemailing.manage.ManagerBase;
import dps.simplemailing.manage.UserManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@ApplicationScoped
public class UserManagerTest extends ManagerTestBase<User> {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addClass(ManagerTestBase.class)
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
    UserManager manager;

    @Override
    protected ManagerBase<User,Long> getManager() {
        return manager;
    }

    @Override
    protected User getTestData(User user) {
        user.setFirstName("Test");
        user.setLastName("User");
        user.setStatus(User.Status.subscribed);
        user.setEmail("test@user.xy");
        return user;
    }

    @Override
    protected void assertTestData(User user) {
        assertEquals(user.getFirstName(), "Test");
        assertEquals(user.getLastName(), "User");
    }

    @Override
    protected void setInvalidData(User user) {
        user.setFirstName(null);
    }

    @Override
    protected void setModifiedData(User user) {
        user.setFirstName("TestModified");
    }

    @Override
    protected void assertModifiedData(User user) {
        assertEquals(user.getFirstName(), "TestModified");
    }

    @Test
    public void testGetActiveAndGetTest() {
        Long cnt = manager.count();

        User activeUser = createTestData();
        long activeId = activeUser.getId();

        User inactiveUser = getTestData(new User());
        inactiveUser.setEmail("inactive@user.xy");
        inactiveUser.setStatus(User.Status.unsubscribed);
        manager.create(inactiveUser);
        long inactiveId = inactiveUser.getId();

        User testUser = getTestData(new User());
        testUser.setEmail("test@user.xy");
        testUser.setStatus(User.Status.test);
        manager.create(testUser);
        long testId = testUser.getId();

        List<User> activeUsers = manager.getActive();
        Boolean activeUserIncluded = false;
        for (User user: activeUsers) {
            if (user.getId() == activeId) activeUserIncluded = true;
            if (user.getId() == inactiveId) Assert.fail("inactive user included");
            if (user.getId() == testId) Assert.fail("test user included");
        }
        assertTrue(activeUserIncluded);

        List<User> inactiveUsers = manager.getTest();
        Boolean testUserIncluded = false;
        for (User user: inactiveUsers) {
            if (user.getId() == testId) testUserIncluded = true;
            if (user.getId() == inactiveId) Assert.fail("inactive user included");
            if (user.getId() == activeId) Assert.fail("active user included");
        }
        assertTrue(testUserIncluded);

        removeTestData(activeId);
        removeTestData(inactiveId);
        removeTestData(testId);

        assertEquals(cnt, manager.count());
    }

}