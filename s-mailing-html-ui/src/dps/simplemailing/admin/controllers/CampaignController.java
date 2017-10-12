package dps.simplemailing.admin.controllers;

import dps.simplemailing.entities.Campaign;
import dps.simplemailing.entities.Campaign_;
import dps.simplemailing.admin.interceptors.RunInitMethod;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.persistence.metamodel.Attribute;
import javax.ws.rs.Path;

@Path("campaigns")
@ApplicationScoped
@Interceptors({RunInitMethod.class})
public class CampaignController extends CrudController<Campaign,Long> {

    @Override
    protected String getSubfolder() {
        return "campaigns";
    }

    @Override
    protected String getTitle()
    {
        return "Campaigns";
    }

    @Override
    protected Attribute<Campaign,?>[] getExtraAttributes()
    {
        Attribute[] arr = {Campaign_.mails};
        return arr;
    }

}
