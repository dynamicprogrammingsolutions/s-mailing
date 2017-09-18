package test;

import dps.reflect.ReflectHelper;
import dps.simplemailing.back.MailManager;
import dps.simplemailing.entities.Mail;
import java.util.List;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.metamodel.Attribute;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EntitiesTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test1.war")
                .addPackage("dps.simplemailing.entities")
                .addPackage("dps.crud")
                .addPackage("dps.simplemailing.back")
                .addPackage("dps.reflect")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject MailManager mailManager;
    Mail testMail;
    long testId;
    
    private Mail getTestData(Mail mail) {
        mail.setName("Test Mail 1");
        mail.setSubject("Test Mail 1 Subject");
        mail.setBody_text("Test Mail 1 Body");
        mail.setFrom("from@test.com"); 
        return mail;
    }
    
    private Mail getTestData() {
        return getTestData(new Mail());
    }
    
    private Mail createTestData() {
        Mail mail = getTestData();
        mailManager.create(mail);  
        testMail = mail;
        testId = mail.getId();
        return mail;
    }
    
    private void removeTestData() {
        mailManager.remove(testId);
    }
    
    @Test public void testCreateSuccess() {
        System.out.println("Create Mail");
        Mail mail = getTestData();
        mailManager.create(mail);
        assertTrue(mail.getId() > 0);
        mail = mailManager.getById(mail.getId());
        assertEquals(mail.getName(), "Test Mail 1");
        mailManager.remove(mail);
    }
    
    @Test public void testCreateFail() {
        System.out.println("Create Mail");
        Mail mail = getTestData();
        mail.setName(null);
        try {
            mailManager.create(mail);
            fail("Should generate error");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class,e.getClass());
        }
    }
    
    @Test
    public void testGetById() {
        
        createTestData();
        
        long id = testId;
        
        Mail mail = mailManager.getById(id);
        assertEquals(mail.getName(), testMail.getName());

        System.out.println("Test finding not existing entity");
        try {
            mail = mailManager.getById(id+1);
            fail("Should generate error");
        } catch (Exception e) {
            System.out.println(e.toString());
            assertEquals(EntityNotFoundException.class,e.getClass());
        }
        
        removeTestData();
        
    }
    
    @Test public void testModify()
    {
        createTestData();
        
        Mail mail = getTestData();
        mail.setName("Test Mail 1 modified");
        mailManager.modify(testId,mail);

        mail = getTestData();
        mail.setName("Test Mail 1 modified");
        
        try {
            mailManager.modify(testId+1,mail);
            fail("Should generate error");
        } catch (Exception e) {
            System.out.println(e.toString());
            assertEquals(EntityNotFoundException.class,e.getClass());
        }



        mail = getTestData();
        mail.setName(null);
        
        try {
            mailManager.modify(testId,mail);
            fail("Should generate error");
        } catch (Exception e) {
            System.out.println(e.toString());
            assertEquals(IllegalArgumentException.class,e.getClass());
        }
        
        
        removeTestData();
    }
    
    @Test public void testGetAll()
    {
        createTestData();
        List<Mail> mails = mailManager.getAll();
        assertFalse(mails.isEmpty());
        System.out.println("data size: "+mails.size());
        for (Mail mail: mails) {
            for (Attribute<Mail,?> attribute: mailManager.getEM().getMetamodel().entity(Mail.class).getDeclaredAttributes()) {
                if (!attribute.isAssociation())
                    System.out.print(attribute.getName()+": "+ReflectHelper.invokeMethod(Mail.class, ReflectHelper.getGetterName(attribute.getName()), mail)+", ");                
            }
            System.out.println("");
        }
        removeTestData();
    }
    
    @Test public void testGetRange()
    {
        createTestData();
        List<Mail> mails = mailManager.get(0,10);
        assertEquals(mails.size(),10);
        System.out.println("data size: "+mails.size());
        for (Mail mail: mails) {
            for (Attribute<Mail,?> attribute: mailManager.getEM().getMetamodel().entity(Mail.class).getDeclaredAttributes()) {
                if (!attribute.isAssociation())
                    System.out.print(attribute.getName()+": "+ReflectHelper.invokeMethod(Mail.class, ReflectHelper.getGetterName(attribute.getName()), mail)+", ");                
            }
            System.out.println("");
        }
        removeTestData();
    }
    
}