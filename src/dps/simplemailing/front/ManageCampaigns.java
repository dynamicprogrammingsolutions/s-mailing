/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.ControllerBase;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Param;
import dps.servletcontroller.Path;
import dps.servletcontroller.RequestParam;
import dps.simplemailing.back.Crud;
import dps.simplemailing.entities.Campaign;
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
@Path("/campaigns(.*)")
public class ManageCampaigns extends AdminControllerBase {

    @Inject Crud crud;
    @Inject ControllerCrud controllerCrud;

    @Filter
    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException
    {
        requestBean.setTitle("S-Mailing - Campaigns");
        requestBean.setRoot(request.getContextPath()+request.getServletPath()+"/campaigns/"); 
        requestBean.setTemplate("/WEB-INF/templates/template.jsp");
        requestBean.setViewRoot("/WEB-INF/campaigns");
        requestBean.setEntityClass(Campaign.class);
        requestBean.setEntityName("Campaign");

        super.filter(request, response, controller, method, args);

    }
    
    @Path("")
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
            request.setAttribute("form", getForm((Campaign)requestBean.getEntityObject(),requestBean.getRoot()+"new","Create"));
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
            request.setAttribute("form", getForm((Campaign)requestBean.getEntityObject(),requestBean.getRoot()+"edit/"+id,"Modify"));
        }
        return result;
    }
    
    @Path("/delete")
    public String delete(HttpServletRequest request, @RequestParam("id") Long id)
    {
        return controllerCrud.delete(request, id);
    }
    
    Form getForm(Campaign campaign, String action, String submitLabel)
    {
        if (campaign == null) campaign = new Campaign();
        Form form = new Form(action);
        form.addInput(new Input("Name","name",campaign.getName()));
        form.addInput(new Input("Long Name","longName",campaign.getLongName()));
        form.setSubmitLabel(submitLabel);
        return form;        
    }
    
    
}
