package dps.simplemailing.manage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UseEntityManager {
    @PersistenceContext(unitName = "SimpleMailingPU")
    protected EntityManager em;

    public EntityManager getEM()
    {
        return em;
    }

}
