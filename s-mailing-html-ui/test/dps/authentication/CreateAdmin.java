package dps.authentication;

import dps.logging.HasLogger;
import dps.simplemailing.authentication.AuthenticableUserProvider;
import dps.simplemailing.entities.ApplicationUser;
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

@RunWith(Arquillian.class)
@ApplicationScoped
public class CreateAdmin {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addPackage("dps.authentication")
                .addPackage("dps.simplemailing.authentication")
                .addPackage("dps.simplemailing.entities")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    AuthenticationManager authenticationManager;

    @Inject
    AuthenticableUserProvider userProvider;

    @Test
    public void addAdmin()
    {
        userProvider.addUser("mtp","***REMOVED***");
        Assert.assertTrue(authenticationManager.login("mtp", "***REMOVED***"));

    }

}
