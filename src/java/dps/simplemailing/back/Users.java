/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ferenci84
 */
@Stateless
public class Users extends Crud<User> {

    public Users()
    {
        super(User.class);
    }
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
    public void create(User entity) {
        em.persist(entity);
    }

    public void edit(User entity) {
        em.merge(entity);
    }

    public void remove(User entity) {
        em.remove(em.merge(entity));
    }

    public User find(Object id) {
        return em.find(User.class, id);
    }

    public List<User> getAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return em.createQuery(cq).getResultList();
    }
    */

}
