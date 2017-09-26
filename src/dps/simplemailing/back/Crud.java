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
