package dps.simplemailing.front.admin.controller;

import dps.servletcontroller.ControllerBase;
import dps.servletcontroller.Filter;
import dps.servletcontroller.Param;
import dps.servletcontroller.Path;
import dps.servletcontroller.RequestParam;
import dps.simplemailing.back.Crud;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.front.admin.ControllerCrud;
import dps.simplemailing.front.forms.Form;
import dps.simplemailing.front.forms.Input;
import dps.simplemailing.front.forms.TextArea;
import dps.simplemailing.manage.MailManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Path("/mails(.*)")
public class ManageMails extends AdminControllerBase {

    @Inject Crud crud;
    @Inject
    ControllerCrud controllerCrud;
    @Inject
    MailManager mails;
    
    @Filter
    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void filter(HttpServletRequest request, HttpServletResponse response, ControllerBase controller, Method method, Object[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException
    {
        
        requestBean.setTitle("S-Mailing - Mails");
        requestBean.setRoot(request.getContextPath()+request.getServletPath()+"/mails/"); 
        requestBean.setTemplate("/WEB-INF/templates/template.jsp");
        requestBean.setViewRoot("/WEB-INF/mails");
        requestBean.setEntityClass(Mail.class);
        requestBean.setEntityName("Mail");

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
    public String newMail(HttpServletRequest request, @RequestParam("id") Long id)
    {
        String result = controllerCrud.newEntity(request, id);
        if (request.getMethod().equals("GET")) {
            request.setAttribute("form", getMailForm((Mail)requestBean.getEntityObject(),requestBean.getRoot()+"new","Create"));
        }
        return result;
    }
    
    
    @Path("/show/(?<id>[0-9]+)")
    public String show(HttpServletRequest request, @Param("id") Long id)
    {
        return controllerCrud.show(request, id);
    }
        
    
    @Path("/edit/(?<id>[0-9]+)")
    public String editMail(HttpServletRequest request, @Param("id") Long id)
    {
        String result = controllerCrud.edit(request, id);
        if (request.getMethod().equals("GET") && requestBean.getEntityObject() != null) {
            request.setAttribute("form", getMailForm((Mail)requestBean.getEntityObject(),requestBean.getRoot()+"edit/"+id,"Modify"));
        }
        return result;
    }
    
    @Path("/delete")
    public String deleteMail(HttpServletRequest request, @RequestParam("id") Long id)
    {
        return controllerCrud.delete(request, id);
    }
    
    @Path("/schedule")
    @Transactional(Transactional.TxType.REQUIRED)
    public String scheduleMail(HttpServletRequest request, @RequestParam("id") Long id)
    {
        Mail mail = crud.find(id,Mail.class);
        if (mail == null) {
            sessionBean.addError("Couldn't find mail with id "+id);
            return "redirect:"+requestBean.getRoot()+"list";
        }
        if (request.getMethod().equals("POST")) {

            try {
            
                int delay = Integer.parseInt(request.getParameter("delay"));
                Boolean real = (request.getParameter("real") != null && request.getParameter("real").equals("on"));
                
                if (real) {
                    System.out.println("sending real mails");
                }

                java.util.Date time = null;
                String timeString = request.getParameter("send_time");
                if (timeString != null && !timeString.isEmpty()) {
                        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        time = fm.parse(timeString);
                }

                mails.scheduleMail(mail, real, time, delay);
                
                sessionBean.addMessage("Mail "+mail.getName()+" Scheduled");

            } catch (ParseException ex) {
                sessionBean.addError("Parse Failed: "+ex.getMessage());
            }

            return "redirect:"+requestBean.getRoot()+"list";
            
        }
        request.setAttribute("item", mail);
        return "/WEB-INF/mails/schedule.jsp";
    }
    
    Form getMailForm(Mail mail, String action, String submitLabel)
    {
        if (mail == null) mail = new Mail();
        Form form = new Form(action);
        form.addInput(new Input("Name","name",mail.getName()));
        form.addInput(new Input("Subject","subject",mail.getSubject()));
        form.addInput(new Input("From Email","from",mail.getFrom()));
        form.addInput(new TextArea("Body Text","body_text",mail.getBody_text()));
        form.setSubmitLabel(submitLabel);
        return form;        
    }
    
    
    
}
