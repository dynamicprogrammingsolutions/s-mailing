/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.Mail;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ferenci84
 */
@Stateless
public class Mails extends Crud<Mail> {
    
    public Mails()
    {
        super(Mail.class);
    }
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    protected EntityManager getEntityManager() {
        return em;
    }

    /*public void create(Mail entity) {
        em.persist(entity);
    }

    public void edit(Mail entity) {
        em.merge(entity);
    }

    public void remove(Mail entity) {
        em.remove(em.merge(entity));
    }

    public Mail find(Object id) {
        return em.find(Mail.class, id);
    }

    public List<Mail> getAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Mail.class));
        return em.createQuery(cq).getResultList();
    }*/

}
