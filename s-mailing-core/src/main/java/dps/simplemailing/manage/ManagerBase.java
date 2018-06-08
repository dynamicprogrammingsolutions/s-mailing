package dps.simplemailing.manage;

import dps.logging.HasLogger;
import dps.commons.reflect.ReflectHelper;
import dps.simplemailing.entities.EntityBase;

import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityNotFoundException;
import javax.persistence.metamodel.Attribute;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class ManagerBase<EntityType extends EntityBase<IdType>,IdType> extends UseEntityManager implements HasLogger {

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
        setLogLevel(Level.FINE);
        Set<ConstraintViolation<EntityType>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<EntityType> constraintViolation: constraintViolations) {
                logFine(constraintViolation.getPropertyPath()+" "+constraintViolation.getMessage());
                System.out.println(constraintViolation.getPropertyPath()+" "+constraintViolation.getMessage());
            }

            throw new IllegalArgumentException("Validation failed");
        }
        em.persist(entity);
    }

    private EntityGraph<EntityType> getEntityGraph(Attribute<EntityType, ?>... attributes)
    {
        EntityGraph<EntityType> entityGraph = em.createEntityGraph(entityClass);
        for (Attribute<EntityType,?> attr: attributes) {
            entityGraph.addAttributeNodes(attr);
        }
        return entityGraph;
    }

    private EntityGraph<EntityType> getEntityGraph(String... attributes)
    {
        EntityGraph<EntityType> entityGraph = em.createEntityGraph(entityClass);
        for (String attr: attributes) {
            entityGraph.addAttributeNodes(attr);
        }
        return entityGraph;
    }

    private Map<String,Object> getLoadGraph(String... attributes)
    {
        Map<String,Object> props = new HashMap<>();
        props.put("javax.persistence.loadgraph",getEntityGraph(attributes));
        return props;
    }

    private Map<String,Object> getLoadGraph(Attribute<EntityType, ?>... attributes)
    {
        Map<String,Object> props = new HashMap<>();
        props.put("javax.persistence.loadgraph",getEntityGraph(attributes));
        return props;
    }

    public EntityType getById(IdType id)
    {
        EntityType entity = em.find(entityClass,id);
        if (entity == null) throw new EntityNotFoundException("Cannot find entity type "+entityClass.getSimpleName()+" with id "+id);
        return entity;
    }

    public EntityType getById(IdType id, String... attributes)
    {
        EntityType entity = em.find(entityClass,id,getLoadGraph(attributes));
        if (entity == null) throw new EntityNotFoundException("Cannot find entity type "+entityClass.getSimpleName()+" with id "+id);
        return entity;
    }

    public EntityType getById(IdType id, Attribute<EntityType, ?>... attributes)
    {
        EntityType entity = em.find(entityClass,id,getLoadGraph(attributes));
        if (entity == null) throw new EntityNotFoundException("Cannot find entity type "+entityClass.getSimpleName()+" with id "+id);
        return entity;
    }


    @Transactional(TxType.REQUIRED)
    public EntityType reload(EntityType entity)
    {
        IdType id = entity.getId();
        entity = em.find(entityClass,entity.getId());
        if (entity == null) throw new EntityNotFoundException("Cannot find entity type "+entityClass.getSimpleName()+" with id "+id);
        return entity;
    }

    @Transactional(TxType.REQUIRED)
    public EntityType reload(EntityType entity, String... attributes)
    {
        IdType id = entity.getId();
        entity = em.find(entityClass,entity.getId(),getLoadGraph(attributes));
        if (entity == null) throw new EntityNotFoundException("Cannot find entity type "+entityClass.getSimpleName()+" with id "+id);
        return entity;
    }


    @Transactional(TxType.REQUIRED)
    public EntityType reload(EntityType entity, Attribute<EntityType, ?>... attributes)
    {
        IdType id = entity.getId();
        entity = em.find(entityClass,entity.getId(),getLoadGraph(attributes));
        if (entity == null) throw new EntityNotFoundException("Cannot find entity type "+entityClass.getSimpleName()+" with id "+id);
        return entity;
    }

    @Transactional(TxType.REQUIRED)
    public EntityType getReference(EntityType entity)
    {
        return em.getReference(entityClass,entity.getId());
    }

    @Transactional(TxType.REQUIRED)
    public void modify(IdType id, EntityType entity) {
        EntityType old = em.getReference(entityClass,id);
        entity.setId(old.getId());
        Set<ConstraintViolation<EntityType>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            logFine("validation failed");
            for (ConstraintViolation<EntityType> constraintViolation: constraintViolations) {
                logFine(constraintViolation.getMessage()+" "+constraintViolation.getPropertyPath());
            }
            throw new IllegalArgumentException("Validation failed");
        }
        em.merge(entity);
    }

    @Transactional(TxType.REQUIRED)
    public void modify(EntityType entity) {
        EntityType old = em.getReference(entityClass,entity.getId());
        Set<ConstraintViolation<EntityType>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }
        em.merge(entity);
    }

    @Transactional(TxType.REQUIRED)
    public void remove(IdType id) throws IllegalArgumentException {
        EntityType entity = em.find(entityClass,id);
        if (entity == null) throw new EntityNotFoundException("Cannot find entity type "+entityClass.getSimpleName()+" with id "+id);
        em.remove(entity);
    }

    @Transactional(TxType.REQUIRED)
    public void remove(EntityType entity) throws IllegalArgumentException {
        this.remove(entity.getId());
    }

    public List<EntityType> get(int first, int max)
    {
        return em.createNamedQuery(queryName("getAll"),entityClass).setFirstResult(first).setMaxResults(max).getResultList();
    }

    public List<EntityType> getAll()
    {
        return em.createNamedQuery(queryName("getAll"),entityClass).getResultList();
    }

    public Long count()
    {
        return em.createNamedQuery(queryName("count"),Long.class).getSingleResult();
    }

    protected String queryName(String name)
    {
        return entityClass.getSimpleName()+"."+name;
    }

    public EntityType newEntity()
    {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
