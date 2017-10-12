package dps.crud;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */
@SuppressWarnings("unchecked")
public abstract class Crud {

    protected abstract EntityManager getEntityManager();
    
    @Transactional(Transactional.TxType.MANDATORY)
    public void create(Object entity) {
        getEntityManager().persist(entity);
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public void edit(Object entity) {
        getEntityManager().merge(entity);
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public void remove(Object entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public <T> T find(Object id, Class<T> entityClass) {
        return getEntityManager().find(entityClass, id);
    }
    
    public <T> List<T> getAll(Class<T> entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
    /*
    public List<?> getAll(Class<?> entityClass) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
    */
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
