package dps.simplemailing.manage;

import dps.simplemailing.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserManager extends ManagerBase<User,Long> {

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
        Query query = em.createQuery("SELECT u FROM User u WHERE u.status = :status");
        query.setParameter("status", User.Status.subscribed);
        return query.getResultList();
    }

    public List<User> getTest()
    {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.status = :status");
        query.setParameter("status", User.Status.test);
        return query.getResultList();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void unsubscribe(User user)
    {
        user.setStatus(User.Status.unsubscribed);
        em.merge(user);
    }

    public User getByEmail(String email)
    {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        if (users.isEmpty()) return null;
        else return users.get(0);
    }

}
