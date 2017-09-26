package dps.simplemailing.back;

import dps.simplemailing.entities.Campaign;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
public class Campaigns {

    @Inject Crud crud;
    
    public Campaign getByName(String name)
    {
        Query query = crud.getEntityManager().createQuery("SELECT u FROM Campaign u WHERE u.name = :name");
        query.setParameter("name", name);
        List<Campaign> campaigns = query.getResultList();
        if (campaigns.isEmpty()) return null;
        else return campaigns.get(0);
    }

}
