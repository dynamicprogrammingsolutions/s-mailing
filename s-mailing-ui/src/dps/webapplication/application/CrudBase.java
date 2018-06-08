package dps.webapplication.application;

import dps.commons.reflect.ReflectHelper;
import dps.webapplication.messages.Messages;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class CrudBase<EntityType, IdType, EntityDAO, EntityListDAO> {

    @Inject
    Validator validator;

    @Inject
    Messages messages;

    protected Class<EntityType> entityClass = null;
    protected Class<IdType> idClass = null;
    protected Class<EntityDAO> entityDAOClass = null;
    protected Class<EntityListDAO> entityListDAOClass = null;

    public CrudBase() {
        Type[] typeParameters = ReflectHelper.getTypeParameters(this.getClass());
        entityClass = (Class<EntityType>)typeParameters[0];
        idClass = (Class<IdType>)typeParameters[1];
        entityDAOClass = (Class<EntityDAO>)typeParameters[2];
        entityListDAOClass = (Class<EntityListDAO>)typeParameters[3];
    }

    protected abstract EntityManager getEM();

    protected <T> TypedQuery<T> setParameters(TypedQuery<T> query) {
        return query;
    }

    protected IdType getEntityId(EntityType entity) {
        return (IdType)ReflectHelper.getEntityId(getEM(),entity);
    }

    protected void processBeforeCreate(EntityType entity) {}

    protected void processBeforeDelete(EntityType entity) {}

    protected void processBeforeEdit(EntityType originalEntity, EntityType editedEntity) {}

    protected void processBeforeFind(EntityType entity) {}

    protected String queryName(String name)
    {
        return entityClass.getSimpleName()+"."+name;
    }

    @GET
    @Path("/")
    @Transactional(Transactional.TxType.REQUIRED)
    public List<EntityListDAO> getRange(@QueryParam("first") Integer first, @QueryParam("max") Integer max)
    {
        try {
            List<EntityType> list = setParameters(getEM().createNamedQuery(queryName("getAll"), entityClass))
                    .setFirstResult(first)
                    .setMaxResults(max)
                    .getResultList();

            if (entityListDAOClass.isAssignableFrom(entityClass)) {

                return (List<EntityListDAO>) list;

            } else {

                List<EntityListDAO> listDAO = new ArrayList<>();

                try {
                    Constructor<EntityListDAO> constructor = entityListDAOClass.getConstructor(entityClass);
                    for (EntityType a : list) {
                        listDAO.add(constructor.newInstance(a));
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new WebApplicationException(e);
                }

                return listDAO;
            }
        } catch (Exception e) {
            messages.addMessage(Messages.Type.Error,entityClass.getSimpleName()+" couldn't be queried. Error: "+e.getMessage());
            throw e;
        }
    }

    @GET
    @Path("/count")
    public IdType getCount()
    {
        return setParameters(getEM().createNamedQuery(queryName("count"),idClass))
                .getSingleResult();
    }

    @GET
    @Path("/{id:\\d+}")
    @Transactional(Transactional.TxType.REQUIRED)
    public EntityDAO find(@PathParam("id") Long id)
    {
        EntityType entity;
        try {
            entity = setParameters(getEM().createNamedQuery(queryName("getById"), entityClass))
                    .setParameter("id", id)
                    .getSingleResult();
            this.processBeforeFind(entity);
            if (entityDAOClass.isAssignableFrom(entityClass)) {
                return (EntityDAO) entity;
            } else {
                Constructor<EntityDAO> constructor = entityDAOClass.getConstructor(entityClass);
                EntityDAO jsonEntity = constructor.newInstance(entity);
                return jsonEntity;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new WebApplicationException(e);
        } catch (NoResultException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            messages.addMessage(Messages.Type.Error,entityClass.getSimpleName()+" couldn't be queried. Error: "+e.getMessage());
            throw e;
        }
    }

    @POST
    @Path("/")
    @Transactional(Transactional.TxType.REQUIRED)
    public IdType create(EntityType entity)
    {
        processBeforeCreate(entity);
        checkValidation(entity);
        getEM().persist(entity);
        try {
            getEM().flush();
            messages.addMessage(Messages.Type.Success,entityClass.getSimpleName()+" added with id: "+getEntityId(entity));
            return getEntityId(entity);
        } catch (Exception e) {
            messages.addMessage(Messages.Type.Error,entityClass.getSimpleName()+" couldn't be created. Error: "+e.getMessage());
            throw e;
        }

        /*Article.JsonArticle jsonEntity = new Article.JsonArticle(entity);
        return jsonEntity;*/
    }

    @PUT
    @Path("/{id}")
    @Transactional(Transactional.TxType.REQUIRED)
    public void edit(@PathParam("id") IdType id, EntityType editedEntity) {
        if (!id.equals(getEntityId(editedEntity))) throw new BadRequestException();
        try {
            EntityType entity = getEM().find(entityClass, id);
            this.processBeforeEdit(entity, editedEntity);
            if (entity == null) throw new NotFoundException();
            checkValidation(editedEntity);
            getEM().merge(editedEntity);
            getEM().flush();
            messages.addMessage(Messages.Type.Success, entityClass.getSimpleName() + " edited with id: " + id);
        } catch (NotFoundException | BadRequestException e) {
          throw e;
        } catch (Exception e) {
            messages.addMessage(Messages.Type.Error,entityClass.getSimpleName()+" couldn't be edited. Error: "+e.getMessage());
            throw e;
        }
    }

    @DELETE
    @Path("/{id:\\d+}")
    @Transactional(Transactional.TxType.REQUIRED)
    public void remove(@PathParam("id") Long id)
    {
        try {
            EntityType entity;
            entity = setParameters(getEM().createNamedQuery(queryName("getById"), entityClass))
                    .setParameter("id", id)
                    .getSingleResult();
            processBeforeDelete(entity);
            getEM().remove(entity);
            getEM().flush();
            messages.addMessage(Messages.Type.Success, entityClass.getSimpleName() + " deleted with id: " + id);
        } catch (NoResultException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            messages.addMessage(Messages.Type.Error,entityClass.getSimpleName()+" couldn't be removed. Error: "+e.getMessage());
            throw e;
        }
    }

    private void checkValidation(EntityType entity)
    {
        Set<ConstraintViolation<EntityType>> violations = validator.validate(entity);
        if (violations.size() > 0) {
            for (ConstraintViolation<EntityType> violation: violations) {
                String propertyName = null;
                for (javax.validation.Path.Node node: violation.getPropertyPath()) {
                    if (propertyName == null) propertyName = node.getName();
                    else propertyName = propertyName + "." + node.getName();
                }
                messages.addMessage(Messages.Type.Error,propertyName+": "+violation.getMessage());
            }
            throw new BadRequestException();
        }
    }

}
