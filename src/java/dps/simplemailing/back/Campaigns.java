/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

/**
 *
 * @author ferenci84
 */
@Stateless
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
