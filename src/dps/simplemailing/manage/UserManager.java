package dps.simplemailing.manage;

import dps.simplemailing.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserManager extends ManagerBase<User,Long> {

    public Map<String,String> getPlaceholders(User user)
    {
        Map<String,String> placeholders = new HashMap<>();
        placeholders.put("firstname", user.getFirstName());
        placeholders.put("email", user.getEmail());
        placeholders.put("id", user.getId().toString());
        return placeholders;
    }

    public List<User> getActive()
    {
        TypedQuery<User> query = em.createNamedQuery("User.getWithStatus",User.class);
        query.setParameter("status", User.Status.subscribed);
        return query.getResultList();
    }

    public List<User> getTest()
    {
        TypedQuery<User> query = em.createNamedQuery("User.getWithStatus",User.class);
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
        TypedQuery<User> query = em.createNamedQuery("User.getByEmail",User.class);
        query.setParameter("email", email);
        User entity = null;
        try {
            entity = query.getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

}
