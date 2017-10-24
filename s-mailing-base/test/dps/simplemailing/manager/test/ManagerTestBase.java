package dps.simplemailing.manager.test;

import dps.simplemailing.entities.EntityBase;
import dps.simplemailing.manage.ManagerBase;
import dps.reflect.ReflectHelper;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;

public abstract class ManagerTestBase<EntityType extends EntityBase<Long>> {

    Class<EntityType> entityClass;

    ManagerTestBase() {
        entityClass = ReflectHelper.getTypeParameter(this.getClass(),0);
    }

    abstract protected ManagerBase<EntityType,Long> getManager();
    abstract protected EntityType getTestData(EntityType mail);
    abstract protected void assertTestData(EntityType mail);
    abstract protected void setInvalidData(EntityType mail);
    abstract protected void setModifiedData(EntityType mail);
    abstract protected void assertModifiedData(EntityType mail);

    @SuppressWarnings("unchecked")
    public EntityType getTestData() {
        EntityType entity = (EntityType)ReflectHelper.newInstance(ReflectHelper.getTypeParameter(this.getClass(),0));
        return getTestData(entity);
    }

    public EntityType createTestData() {
        EntityType mail = getTestData();
        getManager().create(mail);
        return mail;
    }

    public void removeTestData(Long id) {
        getManager().remove(id);
    }


    @Test
    public void testCreateSuccess() {
        Long cnt = getManager().count();

        EntityType mail = getTestData();
        getManager().create(mail);
        assertTrue(mail.getId() >= 0);
        mail = getManager().getById(mail.getId());
        assertTestData(mail);
        removeTestData(mail.getId());

        assertEquals(cnt, getManager().count());
    }


    @Test
    public void testCreateFail() {
        Long cnt = getManager().count();

        EntityType mail = getTestData();
        setInvalidData(mail);
        try {
            getManager().create(mail);
            fail("Should generate error");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class,e.getClass());
        }

        assertEquals(cnt, getManager().count());
    }


    @Test
    public void testGetById() {

        Long cnt = getManager().count();

        EntityType testMail = createTestData();
        long testId = testMail.getId();

        EntityType mail = getManager().getById(testId);
        assertTestData(mail);

        try {
            mail = getManager().getById(testId+1);
            fail("Should generate error");
        } catch (Exception e) {
            assertEquals(EntityNotFoundException.class,e.getClass());
        }

        removeTestData(testId);

        assertEquals(cnt, getManager().count());

    }


    @Test
    public void testModify()
    {
        Long cnt = getManager().count();

        EntityType testMail = createTestData();
        long testId = testMail.getId();

        EntityType mail = getTestData();
        setModifiedData(mail);
        getManager().modify(testId,mail);
        mail = getManager().getById(testId);
        assertModifiedData(mail);

        mail = getTestData();
        setModifiedData(mail);

        try {
            getManager().modify(testId+1,mail);
            fail("Should generate error");
        } catch (Exception e) {
            assertEquals(EntityNotFoundException.class,e.getClass());
        }


        mail = getTestData();
        setInvalidData(mail);

        try {
            getManager().modify(testId,mail);
            fail("Should generate error");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class,e.getClass());
        }

        removeTestData(testId);

        assertEquals(cnt, getManager().count());
    }


    @Test
    public void testGetRange()
    {
        Long cnt = getManager().count();

        List<EntityType> testMails = new ArrayList<>();

        for (int i = 0; i != 5; i++) {
            testMails.add(createTestData());
        }

        List<EntityType> mails = getManager().get(0,5);
        assertEquals(mails.size(),5);

        for (EntityType mail: mails) {
            for (Attribute<EntityType,?> attribute: getManager().getEM().getMetamodel().entity(entityClass).getDeclaredAttributes()) {
                if (!attribute.isAssociation())
                    System.out.print(attribute.getName()+": "+ReflectHelper.invokeMethod(entityClass, ReflectHelper.getGetterName(attribute.getName()), mail)+", ");
            }
        }

        for (EntityType mail: testMails) {
            removeTestData(mail.getId());
        }

        assertEquals(cnt, getManager().count());

    }


}
