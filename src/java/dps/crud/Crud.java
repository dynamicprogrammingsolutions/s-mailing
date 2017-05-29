/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.crud;

import dps.simplemailing.back.*;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author ferenci84
 */
public abstract class Crud {

    protected abstract EntityManager getEntityManager();
    
    public void create(Object entity) {
        getEntityManager().persist(entity);
    }

    public void edit(Object entity) {
        getEntityManager().merge(entity);
    }

    public void remove(Object entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public Object find(Object id, Class<?> entityClass) {
        return getEntityManager().find(entityClass, id);
    }

    public List<?> getAll(Class<?> entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
    
}
