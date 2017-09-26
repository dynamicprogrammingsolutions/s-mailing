package dps.simplemailing.front;

import dps.crud.Paginator;
import dps.servletcontroller.Param;
import dps.servletcontroller.RequestParam;
import dps.simplemailing.back.Crud;
import dps.simplemailing.entities.Mail;
import dps.simplemailing.front.forms.ProcessForm;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
public class ControllerCrud {

    @Inject Crud crud;
    @Inject RequestBean requestBean;
    @Inject SessionBean sessionBean;
    @Inject ProcessForm processForm;
    
    public String list(HttpServletRequest request, Integer page)
    {
        if (page == null) page = 0;
        int resultsPerPage = sessionBean.getResultsPerPage();
        int firstResult = page*resultsPerPage;
        List<?> allMails = crud.getPaginated(requestBean.getEntityClass(),firstResult,resultsPerPage);
        Long count = crud.getCount(requestBean.getEntityClass());
        
        Paginator paginator = new Paginator(page,resultsPerPage,count,requestBean.getRoot()+"list/");
        
        request.setAttribute("paginator", paginator);
        request.setAttribute("items", allMails);
        
        return requestBean.getViewRoot()+"/list.jsp";
    }
    
    public String newEntity(HttpServletRequest request, @RequestParam("id") Long id)
    {
        Object entity = null;
    
        if (id != null) {
            entity = crud.find(id,requestBean.getEntityClass());
            requestBean.setEntityObject(entity);
            if (entity == null) {
                sessionBean.addError("Couldn't find "+requestBean.getEntityName()+" with id "+id);
            }
        }
        
        if (entity == null) {
            try {
                entity = requestBean.getEntityClass().getConstructor().newInstance();                
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ControllerCrud.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (request.getMethod().equals("POST")) {
            
            processForm.process(entity, request.getParameterMap());
            crud.create(entity);
            sessionBean.addMessage(requestBean.getEntityName()+" Created");
            return "redirect:"+requestBean.getRoot()+"list";
        }

        return requestBean.getViewRoot()+"/new.jsp";
    }
    
    public String show(HttpServletRequest request, Long id)
    {
        Object entity = crud.find(id,requestBean.getEntityClass());
        if (entity == null) {
            sessionBean.addError("Couldn't find "+requestBean.getEntityName()+" with id "+id);
        } else {
            request.setAttribute("item", entity); 
        }
        return requestBean.getViewRoot()+"/show.jsp";
    }
    
    public String edit(HttpServletRequest request, Long id)
    {
        Object entity = crud.find(id,requestBean.getEntityClass());
        requestBean.setEntityObject(entity);
        
        if (entity == null) {
            sessionBean.addError("Couldn't find "+requestBean.getEntityName()+" with id "+id);
            return "redirect:"+requestBean.getRoot()+"list";
        }
        
        if (request.getMethod().equals("POST")) {
            processForm.process(entity,request.getParameterMap());
            crud.edit(entity);
            sessionBean.addMessage(requestBean.getEntityName()+" Modified");
            return "redirect:"+requestBean.getRoot()+"show/"+id;
        }
        
        return requestBean.getViewRoot()+"/edit.jsp";
    }
    
    public String delete(HttpServletRequest request, Long id)
    {
        if (request.getMethod().equals("POST")) {
            Object mail = crud.find(id,requestBean.getEntityClass());
            if (mail == null) {
                sessionBean.addError("Couldn't find "+requestBean.getEntityName()+" with id "+id);
            } else {
                crud.remove(mail);
                sessionBean.addMessage(requestBean.getEntityName()+" deleted");
            }
        }
        return "redirect:"+requestBean.getRoot()+"list";
    }
}
