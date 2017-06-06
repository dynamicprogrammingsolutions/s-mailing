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
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
@Path("/mails/(.*)")
public class ManageMails extends ControllerBase {

    @Inject Crud crud;
    
    @Filter
    public void filter(HttpServletRequest request, HttpServletResponse response, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("testfilter");
        
        String result = (String)method.invoke(this, args);
        
        writer.println(result);
        
    }
     
    @Path("new")
    public String showUsers() throws IOException
    {
        return "result";
    }
}
