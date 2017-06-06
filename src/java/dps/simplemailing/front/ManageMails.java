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
import dps.simplemailing.entities.Mail;
import dps.simplemailing.front.forms.Form;
import dps.simplemailing.front.forms.Input;
import dps.simplemailing.front.forms.ProcessForm;
import dps.simplemailing.front.forms.TextArea;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ferenci84
 */
@Stateless
@Path("/mails(.*)")
public class ManageMails extends ControllerBase {

    @Inject RequestBean requestBean;
    @Inject SessionBean sessionBean;
    
    @Inject Crud crud;
    
    @Inject ProcessForm processForm;
    
    @Filter
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException
    {
        if (method.getReturnType().equals(String.class)) {
            
            requestBean.setRoot(request.getContextPath()+request.getServletPath()+"/mails/");          
            
            System.out.println("controller: "+controller.getClass().getName());
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

                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/templates/template.jsp");
                    requestDispatcher.forward(request, response);
                    
                }
            }
        }
        
    }
     
    @Path("/test")
    public String test(HttpServletRequest request) throws IOException
    {
        request.setAttribute("valami", "valami1");
        
        sessionBean.addError("Test Error");
        requestBean.setTitle("Test Title");
        
        return "/test.jsp";
    }
    
    @Path("")
    public String index(HttpServletRequest request)
    {
        return this.list(request);
    }
    
    @Path("/")
    public String index1(HttpServletRequest request)
    {
        return this.list(request);
    }
    
    @Path("/list")
    public String list(HttpServletRequest request)
    {
        List<Mail> allMails = (List<Mail>)crud.getAll(Mail.class);
        request.setAttribute("mails", allMails);
        return "/mails/list.jsp";
    }
    
    @Path("/new")
    public String newMail(HttpServletRequest request, @RequestParam("id") Long id)
    {
        Mail mail = null;
    
        if (id != null) {
            mail = (Mail)crud.find(id,Mail.class);
            if (mail == null) {
                sessionBean.addError("Couldn't find mail with id "+id);
            }
        }
        
        if (mail == null) mail = new Mail();
        
        if (request.getMethod().equals("POST")) {
            
            processForm.process(mail, request.getParameterMap());
            crud.create(mail);
            sessionBean.addMessage("Mail Created with id "+mail.getId());
            return "redirect:"+requestBean.getRoot()+"show/"+mail.getId();
        }

        request.setAttribute("form", getMailForm(mail,requestBean.getRoot()+"new","Create"));
        
        return "/mails/new.jsp";
    }
    
    
    @Path("/show/(?<id>[0-9]+)")
    public String show(HttpServletRequest request, @Param("id") Long id)
    {
        Mail mail = (Mail)crud.find(id,Mail.class);
        if (mail == null) {
            sessionBean.addError("Couldn't find mail with id "+id);
        } else {
            request.setAttribute("item", mail); 
        }
        return "/mails/show.jsp";
    }
        
    
    @Path("/edit/(?<id>[0-9]+)")
    public String editMail(HttpServletRequest request, @Param("id") Long id)
    {
        Mail mail = (Mail)crud.find(id,Mail.class);
        if (mail == null) {
            sessionBean.addError("Couldn't find mail with id "+id);
        }
        
        if (request.getMethod().equals("POST")) {
            processForm.process(mail,request.getParameterMap());
            crud.edit(mail);
            sessionBean.addMessage("Mail Modified");
            return "redirect:"+requestBean.getRoot()+"show/"+mail.getId();
        }
        
        if (mail != null) {
            request.setAttribute("form", getMailForm(mail,requestBean.getRoot()+"edit/"+mail.getId(),"Modify"));
        }
        
        return "/mails/edit.jsp";
    }
    
    @Path("/delete")
    public String deleteMail(HttpServletRequest request, @RequestParam("id") Long id)
    {
        if (request.getMethod().equals("POST")) {
            Mail mail = (Mail)crud.find(id,Mail.class);
            if (mail == null) {
                sessionBean.addError("Couldn't find mail with id "+id);
            } else {
                crud.remove(mail);
                sessionBean.addMessage("Mail deleted");
            }
        }
        return "redirect:"+requestBean.getRoot()+"list";
    }
    
    Form getMailForm(Mail mail, String action, String submitLabel)
    {
        Form form = new Form(action);
        form.addInput(new Input("Name","name",mail.getName()));
        form.addInput(new Input("Subject","subject",mail.getSubject()));
        form.addInput(new Input("From Email","from",mail.getFrom()));
        form.addInput(new TextArea("Body Text","body_text",mail.getBody_text()));
        form.setSubmitLabel(submitLabel);
        return form;        
    }
    
    
    
}
