package dps.simplemailing.helloworld;

import dps.simplemailing.entities.User;
import dps.simplemailing.entities.User_;
import dps.simplemailing.manage.UserManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.ExecutionException;

@RunWith(Arquillian.class)
public class HelloWorldWithDBArquillianTest {


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addPackage("dps.simplemailing.entities")
                .addPackage("dps.simplemailing.manage")
                .addPackage("dps.simplemailing.mailqueue")
                .addPackage("dps.logging")
                .addPackage("dps.commons.reflect")
                .addPackage("dps.commons.configuration")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("s-mailing.conf.xml", "META-INF/s-mailing.conf.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    UserManager manager;

    @Test
    public void helloTest()
    {

        User user = new User();
        user.setStatus(User.Status.subscribed);
        user.setEmail("ferenci84@gmail.com");
        user.setFirstName("Zoltan Laszlo");
        user.setLastName("Ferenci");
        manager.create(user);
        Assert.assertNotNull(user.getId());
        user = manager.getById(user.getId());
        Assert.assertNotNull(user);
        long id = user.getId();
        manager.remove(user);
        try {
            user = manager.getById(id);
        } catch (EntityNotFoundException e) {
            user = null;
        }
        Assert.assertNull(user);

    }

}
