/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ferenci84
 */
@Dependent
public class Crud extends dps.crud.Crud {
    
    @PersistenceContext(unitName = "SimpleMailingPU")
    private EntityManager em;
    public EntityManager getEntityManager() {
        return em;
    }

}
