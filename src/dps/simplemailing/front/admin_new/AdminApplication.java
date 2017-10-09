package dps.simplemailing.front.admin_new;

import dps.simplemailing.front.admin_new.controllers.CampaignController;
import dps.simplemailing.front.admin_new.controllers.MailsController;
import dps.simplemailing.front.admin_new.controllers.SeriesController;
import dps.simplemailing.front.admin_new.filters.SetRequestAttributesFilter;
import dps.simplemailing.front.admin_new.filters.TemplateFilter;
import dps.simplemailing.front.admin_new.provider.EntityReader;
import dps.simplemailing.front.admin_new.provider.ViewProvider;
import dps.simplemailing.rs.ForwardWriter;
import dps.simplemailing.rs.RedirectProvider;
import dps.simplemailing.rs.ResponseSentWriter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/admin")
public class AdminApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(ForwardWriter.class);
        set.add(RedirectProvider.class);
        set.add(ViewProvider.class);
        set.add(EntityReader.class);
        set.add(TemplateFilter.class);
        set.add(SetRequestAttributesFilter.class);
        set.add(ResponseSentWriter.class);
        set.add(MailsController.class);
        set.add(CampaignController.class);
        set.add(SeriesController.class);
        return set;
    }
}
