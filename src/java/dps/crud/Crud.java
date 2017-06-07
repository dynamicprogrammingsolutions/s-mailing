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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

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
    
    public List<?> getPaginated(Class<?> entityClass, int firstResult, int resultsPerPage) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root root = cq.from(entityClass);
        cq.select(root);
        return getEntityManager().createQuery(cq).setFirstResult(firstResult).setMaxResults(resultsPerPage).getResultList();
    }
    
    public Long getCount(Class<?> entityClass) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root root = cq.from(entityClass);
        cq.select(criteriaBuilder.count(root));
        return getEntityManager().createQuery(cq).getSingleResult();
    }
    
}
