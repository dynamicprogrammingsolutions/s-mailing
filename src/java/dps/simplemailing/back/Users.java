/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

import dps.simplemailing.entities.User;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    public Map<String,String> getPlaceholders(User user)
    {
        Map<String,String> placeholders = new HashMap<String,String>();
        placeholders.put("firstname", user.getFirstName());
        placeholders.put("email", user.getEmail());
        placeholders.put("id", user.getId().toString());
        return placeholders;
    }
    
    public List<User> getActive()
    {
        Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.status = :status");
        query.setParameter("status", User.Status.subscribed);
        return query.getResultList();
    }
    
    public List<User> getTest()
    {
        Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.status = :status");
        query.setParameter("status", User.Status.test);
        return query.getResultList();
    }
    
    public void unsubscribe(User user)
    {
        user.setStatus(User.Status.unsubscribed);
    }

}
