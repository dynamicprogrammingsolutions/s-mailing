package dps.simplemailing.admin.controllers;

import dps.simplemailing.entities.Series;
import dps.simplemailing.entities.Series_;
import dps.simplemailing.admin.interceptors.RunInitMethod;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.persistence.metamodel.Attribute;
import javax.ws.rs.Path;

@Path("series")
@ApplicationScoped
@Interceptors({RunInitMethod.class})
public class SeriesController extends CrudController<Series,Long> {

    @Override
    protected String getSubfolder() {
        return "series";
    }

    @Override
    protected String getTitle()
    {
        return "Series";
    }

    @Override
    protected Attribute<Series,?>[] getExtraAttributes()
    {
        Attribute[] arr = {Series_.seriesItems};
        return arr;
    }

}
