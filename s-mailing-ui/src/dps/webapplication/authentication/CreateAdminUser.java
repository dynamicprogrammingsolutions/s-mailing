package dps.webapplication.authentication;

import dps.commons.startup.Startup;
import dps.logging.HasLogger;
import dps.simplemailing.database.DatabaseSetup;
import dps.webapplication.entities.ApplicationUser;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

@ApplicationScoped
@Startup
public class CreateAdminUser implements HasLogger {

    @PersistenceContext(unitName = "DefaultPersistenceUnit")
    EntityManager em;

    @Resource
    UserTransaction tx;

    @Inject
    DatabaseSetup databaseSetup;

    @PostConstruct
    void init() {
        logInfo("preloading: "+databaseSetup.toString());
        Long count = em.createNamedQuery("ApplicationUser.count", Long.class).getSingleResult();
        if (count == 0) {
            try {
                tx.begin();
                this.addUser("admin", "admin");
                tx.commit();
            } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void addUser(String username, String password)
    {
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setAccessLevel(7);
        em.persist(user);
    }
}
