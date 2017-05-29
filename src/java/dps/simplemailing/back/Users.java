/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.back;

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
public class Users {

    @Inject Crud crud;
    
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
        Query query = crud.getEntityManager().createQuery("SELECT u FROM User u WHERE u.status = :status");
        query.setParameter("status", User.Status.subscribed);
        return query.getResultList();
    }
    
    public List<User> getTest()
    {
        Query query = crud.getEntityManager().createQuery("SELECT u FROM User u WHERE u.status = :status");
        query.setParameter("status", User.Status.test);
        return query.getResultList();
    }
    
    public void unsubscribe(User user)
    {
        user.setStatus(User.Status.unsubscribed);
    }

}
