package dps.simplemailing.admin.controllers;

import dps.simplemailing.admin.interceptors.RunInitMethod;
import dps.simplemailing.admin.views.RequestBean;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Produces(MediaType.TEXT_HTML)
public abstract class AdminControllerBase  {

    @Inject
    RequestBean requestBean;

    @Context
    HttpServletRequest request;

    @Context
    UriInfo uri;

    protected abstract String getSubfolder();

    protected String getTemplate()
    {
        return "/WEB-INF/templates/template.jsp";
    }

    protected String getViewRoot()
    {
        return "/WEB-INF/"+getSubfolder();
    }

    protected String getResourceRoot()
    {
        return request.getContextPath()+"/res";
    }

    protected String getRoot()
    {
        return uri.getBaseUri()+getSubfolder()+"/";
    }

    protected abstract String getTitle();

    public void init()
    {
        requestBean.setTitle(getTitle());
        requestBean.setRoot(getRoot());
        requestBean.serResourceRoot(getResourceRoot());
        requestBean.setTemplate(getTemplate());
    }

}
