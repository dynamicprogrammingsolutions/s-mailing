package dps.simplemailing.front.admin.controller;

import dps.servletcontroller.ControllerBase;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Param;
import dps.servletcontroller.Path;
import dps.servletcontroller.RequestParam;
import dps.simplemailing.entities.Series;
import dps.simplemailing.front.admin.ControllerCrud;
import dps.simplemailing.front.forms.CheckBox;
import dps.simplemailing.front.forms.Form;
import dps.simplemailing.front.forms.Input;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
@Path("/series(.*)")
public class ManageSeries extends AdminControllerBase {

    @Inject
    ControllerCrud controllerCrud;

    @Filter
    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException
    {
        requestBean.setTitle("S-Mailing - Series");
        requestBean.setRoot(request.getContextPath()+request.getServletPath()+"/series/"); 
        requestBean.setTemplate("/WEB-INF/templates/template.jsp");
        requestBean.setViewRoot("/WEB-INF/series");
        requestBean.setEntityClass(Series.class);
        requestBean.setEntityName("Series");

        super.filter(request, response, controller, method, args);

    }
    
    @Path()
    public String index(HttpServletRequest request)
    {
        return this.list(request,0);
    }
    
    @Path("/list(?:/(?<page>[0-9]+))?")
    public String list(HttpServletRequest request, @Param("page") Integer page)
    {
        return controllerCrud.list(request, page);
    }
    
    @Path("/new")
    public String newCampaign(HttpServletRequest request, @RequestParam("id") Long id)
    {
        String result = controllerCrud.newEntity(request, id);
        if (request.getMethod().equals("GET")) {
            request.setAttribute("form", getForm((Series)requestBean.getEntityObject(),requestBean.getRoot()+"new","Create"));
        }
        return result;
    }
    
    
    @Path("/show/(?<id>[0-9]+)")
    public String show(HttpServletRequest request, @Param("id") Long id)
    {
        return controllerCrud.show(request, id);
    }
        
    
    @Path("/edit/(?<id>[0-9]+)")
    public String edit(HttpServletRequest request, @Param("id") Long id)
    {
        String result = controllerCrud.edit(request, id);
        if (request.getMethod().equals("GET") && requestBean.getEntityObject() != null) {
            request.setAttribute("form", getForm((Series)requestBean.getEntityObject(),requestBean.getRoot()+"edit/"+id,"Modify"));
        }
        return result;
    }
    
    @Path("/delete")
    public String delete(HttpServletRequest request, @RequestParam("id") Long id)
    {
        return controllerCrud.delete(request, id);
    }
    
    Form getForm(Series series, String action, String submitLabel)
    {
        if (series == null) series = new Series();
        Form form = new Form(action);
        form.addInput(new Input("Name","name",series.getName()));
        form.addInput(new Input("Display Name","displayName",series.getDisplayName()));
        form.addInput(new CheckBox("Update Subscribe Time","updateSubscribeTime",series.getUpdateSubscribeTime()));
        form.setSubmitLabel(submitLabel);
        return form;        
    }
    
    
}
