package dps.simplemailing.rs;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.POST;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ferenci84
 */
@ApplicationPath("/test")
@Dependent
public class TestApplication extends Application {

    @Inject
    Instance<Object> getSingletons;

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(ForwardWriter.class);
        set.add(ResponseSentWriter.class);
        set.add(RsTest.class);
        return set;
    }

}
