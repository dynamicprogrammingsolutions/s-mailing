package dps.simplemailing.admin.controllers;

import dps.authentication.AuthenticationManager;
import dps.authentication.AuthenticationManagerFactory;
import dps.logging.HasLogger;
import dps.simplemailing.admin.authentication.RestrictedAccess;
import dps.simplemailing.admin.authentication.UsingAuthenticationManager;
import dps.simplemailing.admin.interceptors.AuthenticationInterceptor;
import dps.simplemailing.admin.interceptors.RunInitMethod;
import dps.simplemailing.admin.provider.View;
import dps.simplemailing.admin.views.SessionBean;
import dps.simplemailing.rs.Forward;
import dps.simplemailing.rs.Redirect;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.logging.Logger;

@Path("")
@ApplicationScoped
@Interceptors({RunInitMethod.class,AuthenticationInterceptor.class})
public class AdminController extends AdminControllerBase implements ControllerInit, UsingAuthenticationManager, HasLogger {

    @Inject
    SessionBean sessionBean;

    @Override
    protected String getSubfolder() {
        return "";
    }

    @Override
    protected String getViewRoot()
    {
        return "/WEB-INF/admin";
    }

    @Override
    protected String getTitle()
    {
        return "Admin";
    }

    @Context
    HttpServletRequest request;

    @GET
    @Path("/")
    public Object index()
    {
        if (getAuthenticationManager().isAuthenticated())
            return new View("/WEB-INF/admin/index.jsp");
        else
            logInfo("not logged in: redirecting");
            return new Redirect(this.getRoot()+"login");
    }

    @GET
    @Path("login")
    public View getLogin() {
        request.setAttribute("action",requestBean.getBasePath()+"/login");
        return new View(getViewRoot() + "/login.jsp");
    }

    @POST
    @Path("login")
    public Redirect postLogin(@FormParam("username") String username, @FormParam("password") String password) {
        logFine("trying login with: "+username+" "+password);
        if (getAuthenticationManager().login(username,password)) {
            logInfo("logged in");
            sessionBean.addMessage("logged in");
            return new Redirect(this.getRoot());
        } else {
            sessionBean.addError("login failed");
            return new Redirect(this.getRoot() + "login");
        }
    }

    @POST
    @Path("logout")
    public Redirect postLogout()
    {
        getAuthenticationManager().logout();
        sessionBean.addMessage("logged out");
        return new Redirect(this.getRoot()+"login");
    }

    @GET
    @Path("logout")
    public Redirect getLogout()
    {
        getAuthenticationManager().logout();
        sessionBean.addMessage("logged out");
        return new Redirect(this.getRoot()+"login");
    }

}
