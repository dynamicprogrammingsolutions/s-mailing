package dps.simplemailing.front;

import dps.servletcontroller.ControllerBase;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
public class AdminControllerBase extends ControllerBase {
    @Inject RequestBean requestBean;
    @Inject SessionBean sessionBean;
    
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException
    {
        
        if (method.getReturnType().equals(String.class)) {
            
            String result = (String)method.invoke(controller, args);
            
            if (result != null) {
                
                if (result.startsWith("redirect:")) {
                    
                    response.sendRedirect(result.substring("redirect:".length()));
                    
                } else {

                    request.setAttribute("contents", result);

                    request.setAttribute("errors", sessionBean.getErrors());
                    request.setAttribute("messages", sessionBean.getMessages());
                    request.setAttribute("title", requestBean.getTitle());
                    request.setAttribute("root", requestBean.getRoot());
                    
                    request.setAttribute("contextPath", request.getContextPath());
                    request.setAttribute("servletPath", request.getServletPath());

                    RequestDispatcher requestDispatcher = request.getRequestDispatcher(requestBean.getTemplate());
                    requestDispatcher.forward(request, response);
                    
                }
            }
            
        } else {
            
            method.invoke(controller, args);
            
        }
        
    }
    
}
