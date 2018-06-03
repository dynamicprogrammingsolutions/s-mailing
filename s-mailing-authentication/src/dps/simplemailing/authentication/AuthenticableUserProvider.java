package dps.simplemailing.authentication;

import dps.authentication.AuthenticableUser;
import dps.authentication.UserDataProvider;
import dps.simplemailing.entities.ApplicationUser;

import javax.enterprise.context.Dependent;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Singleton
public class AuthenticableUserProvider implements UserDataProvider {

    @PersistenceContext(unitName = "UserDataPU")
    EntityManager em;

    transient boolean adminUserChecked;

    @Transactional(Transactional.TxType.REQUIRED)
    public void addUser(String username, String password)
    {
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setPassword(password);
        em.persist(user);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public AuthenticableUser getUserByCredentials(String username, String password) {

        if (!adminUserChecked) {
            synchronized (this) {
                if (adminUserChecked) {
                    Long count = em.createNamedQuery("ApplicationUser.count", Long.class).getSingleResult();
                    if (count == 0) {
                        this.addUser("admin", "admin");
                    }
                    adminUserChecked = true;
                }
            }
        }

        TypedQuery<ApplicationUser> query = em.createNamedQuery("ApplicationUser.getByUsername", ApplicationUser.class);
        query.setParameter("username",username);
        try {
            ApplicationUser user = query.getSingleResult();
            if (user.checkCredentials(username,password)) {
                return user;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Boolean checkAuthorization(AuthenticableUser authenticableUser, String s) {
        return true;
    }
}
