/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import dps.servletcontroller.Controller;
import dps.servletcontroller.ControllerParam;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Param;
import dps.servletcontroller.Path;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
@Path(value="/(?<cparam>[0-9]*)/(.*)",pathGroup=2)
public class TestRouter extends Controller {
    
    @Filter
    public void filter(HttpServletRequest request, HttpServletResponse response, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("testfilter");
        
        String result = (String)method.invoke(this, args);
        
        writer.println(result);
        
    }
    
    @Path("test/(?<param>[0-9]*)")
    public String test(HttpServletRequest request, HttpServletResponse response, @Param(group=1) String param, @ControllerParam("cparam") String cparam) throws IOException
    {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("test");
        writer.println(param);
        writer.println(cparam);
        return "result";
    }
    
}
