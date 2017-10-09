package dps.simplemailing.resources;

import dps.simplemailing.rs.RsTest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/resources")
public class ResourcesApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(CampaignResource.class);
        set.add(MailResource.class);
        set.add(SeriesResource.class);
        set.add(UserResource.class);
        return set;
    }
}
