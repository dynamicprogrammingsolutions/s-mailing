/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.ControllerBase;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Path;
import dps.simplemailing.back.Crud;
import dps.simplemailing.back.Mails;
import dps.simplemailing.front.forms.ProcessForm;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
@Path("/campaigns(.*)")
public class ManageCampaigns extends AdminControllerBase {

    @Inject ProcessForm processForm;    
    @Inject Mails mails;
    
    @Filter
    @Override
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException
    {
        requestBean.setTitle("S-Mailing - Campaigns");
        requestBean.setRoot(request.getContextPath()+request.getServletPath()+"/campaigns/"); 
        requestBean.setTemplate("/WEB-INF/templates/template.jsp");

        super.filter(request, response, controller, method, args);

    }
    
}
