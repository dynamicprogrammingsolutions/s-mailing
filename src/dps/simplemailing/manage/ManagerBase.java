package dps.simplemailing.manage;

import dps.reflect.ReflectHelper;
import dps.simplemailing.entities.EntityBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class ManagerBase<EntityType extends EntityBase<IdType>,IdType> extends UseEntityManager {

    /*
    @PersistenceContext(unitName = "SimpleMailingPU")
    protected EntityManager em;

    public EntityManager getEM()
    {
        return em;
    }
    */

    protected Class<EntityType> entityClass;

    @Inject
    Validator validator;

    public ManagerBase()
    {
        entityClass = ReflectHelper.getTypeParameter(getClass(),0);
    }

    @Transactional(TxType.REQUIRED)
    public void create(EntityType entity) throws IllegalArgumentException
    {
        Set<ConstraintViolation<EntityType>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }
        em.persist(entity);
    }


    public EntityType getById(IdType id)
    {
        EntityType entity = em.find(entityClass,id);
        if (entity == null) throw new EntityNotFoundException();
        return entity;
    }



    @Transactional(TxType.REQUIRED)
    public void modify(IdType id, EntityType entity) {
        EntityType old = em.getReference(entityClass,id);
        entity.setId(old.getId());
        Set<ConstraintViolation<EntityType>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }
        em.merge(entity);
    }

    @Transactional(TxType.REQUIRED)
    public void modify(EntityType entity) {
        Set<ConstraintViolation<EntityType>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }
        em.merge(entity);
    }

    @Transactional(TxType.REQUIRED)
    public void remove(IdType id) throws IllegalArgumentException {
        EntityType entity = em.find(entityClass,id);
        em.remove(entity);
    }

    @Transactional(TxType.REQUIRED)
    public void remove(EntityType entity) throws IllegalArgumentException {
        entity = em.getReference(entityClass,entity.getId());
        em.remove(entity);
    }

    public List<EntityType> get(int first, int max)
    {
        return em.createNamedQuery(entityClass.getSimpleName()+".getAll",entityClass).setFirstResult(first).setMaxResults(max).getResultList();
    }

    public List<EntityType> getAll()
    {
        return em.createNamedQuery(entityClass.getSimpleName()+".getAll",entityClass).getResultList();
    }

    public Long count()
    {
        return em.createNamedQuery(entityClass.getSimpleName()+".count",Long.class).getSingleResult();
    }


}
