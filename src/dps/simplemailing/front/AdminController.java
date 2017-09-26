/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.ControllerBase;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Path;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
@Path("(.*)")
public class AdminController extends AdminControllerBase {
    
    @Filter
    @Override
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, InvocationTargetException, ServletException, IllegalArgumentException
    {
        requestBean.setTitle("S-Mailing - Mails");
        requestBean.setRoot(request.getContextPath()+request.getServletPath()+"/mails/"); 
        requestBean.setTemplate("/WEB-INF/templates/template.jsp");

        super.filter(request, response, controller, method, args);

    }
    
    @Path("/")
    public String index()
    {
        return "/WEB-INF/admin/index.jsp";
    }
    
    @Path("")
    public String index1()
    {
        return "/WEB-INF/admin/index.jsp";
    }
    
}