package test;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.arquillian.wildfly.example.Greeter;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BasicTest_ {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test.war")
                .addClass(Greeter.class)
                .addClass(dps.crud.Crud.class)
                .addClass(dps.simplemailing.back.Crud.class)
                .addPackage(dps.simplemailing.entities.User.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Greeter greeter;

    @Test
    public void shouldBeAbleTo() {
        assertEquals("Hello, aliens!", greeter.createGreeting("aliens"));
    }
    
    /*@Test
    public void shouldNotBeAbleTo() {
        assertNotEquals("Hello, aliens!", greeter.createGreeting("aliens"));
    }*/
    
    /*
    @Test
    public void testPersistence() throws Exception {
        //utx.begin();
        //crud.getEntityManager().joinTransaction();
        List<?> items = crud.getAll(User.class);
        System.err.println("got users: "+items.size());
        for (Object item: items) {
            User user = (User)item;
            System.err.println(user);
        }
        
        //utx.commit();
        
    }
    */
}