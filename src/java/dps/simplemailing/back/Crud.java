/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ferenci84
 */
@Stateless
public class Crud extends dps.crud.Crud {
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    protected EntityManager getEntityManager() {
        return em;
    }

}
