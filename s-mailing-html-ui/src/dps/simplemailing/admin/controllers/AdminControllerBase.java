package dps.simplemailing.admin.controllers;

import dps.authentication.AuthenticationManager;
import dps.authentication.AuthenticationManagerFactory;
import dps.simplemailing.admin.authentication.UsingAuthenticationManager;
import dps.simplemailing.admin.config.AdminConfig;
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
public abstract class AdminControllerBase implements UsingAuthenticationManager {

    @Inject
    AuthenticationManagerFactory authenticationManagerFactory;

    public AuthenticationManager getAuthenticationManager() {
        return requestBean.getAuthenticationManager();
    }

    @Inject
    RequestBean requestBean;

    @Context
    HttpServletRequest request;

    @Context
    UriInfo uri;

    @Inject
    AdminConfig adminConfig;

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
        return adminConfig.getResourcePath();
    }

    protected String getBasePath()
    {
        return adminConfig.getBasePath();
    }

    protected String getRoot()
    {
        return adminConfig.getBasePath()+(getSubfolder().isEmpty()?"":"/")+getSubfolder()+"/";
    }

    protected abstract String getTitle();

    public void init()
    {
        requestBean.setTitle(getTitle());
        requestBean.setRoot(getRoot());
        requestBean.serResourceRoot(getResourceRoot());
        requestBean.setTemplate(getTemplate());
        requestBean.setBasePath(getBasePath());
        requestBean.setAuthenticationManager(authenticationManagerFactory.getAuthenticationManager(request.getSession()));
    }

}
