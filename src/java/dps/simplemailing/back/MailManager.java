/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.Mail;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

@Singleton
public class MailManager {
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;

    @Inject Crud crud;
    @Inject Validator validator;
    
    public EntityManager getEM()
    {
        return em;
    }
        
    @Transactional(TxType.REQUIRED)
    public void create(Mail entity) throws IllegalArgumentException {
        Set<ConstraintViolation<Mail>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }
        em.persist(entity);
    }
    
    public Mail getById(Long id) {
        Mail entity = em.find(Mail.class,id);
        if (entity == null) throw new EntityNotFoundException();
        return entity;
    }
    
    @Transactional(TxType.REQUIRED)
    public void modify(Long id, Mail entity) {
        Mail old = em.getReference(Mail.class,id);
        entity.setId(old.getId());
        Set<ConstraintViolation<Mail>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed");
        }
        em.merge(entity);
    }
    
    @Transactional(TxType.REQUIRED)
    public void remove(Long id) throws IllegalArgumentException {
        Mail entity = em.find(Mail.class,id);
        em.remove(entity);
    }
    
    @Transactional(TxType.REQUIRED)
    public void remove(Mail entity) throws IllegalArgumentException {
        entity = em.getReference(Mail.class,entity.getId());
        em.remove(entity);
    }
    
    public List<Mail> get(int first, int max)
    {
        return em.createNamedQuery("Mail.getAll",Mail.class).setFirstResult(first).setMaxResults(max).getResultList();
    }
    
    public List<Mail> getAll()
    {
        return em.createNamedQuery("Mail.getAll",Mail.class).getResultList();
    }
}
