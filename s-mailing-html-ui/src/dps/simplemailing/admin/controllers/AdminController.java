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
@Dependent
@Interceptors({RunInitMethod.class,AuthenticationInterceptor.class})
public class AdminController extends AdminControllerBase implements ControllerInit, UsingAuthenticationManager, HasLogger {

    @Inject
    AuthenticationManagerFactory authenticationManagerFactory;

    AuthenticationManager authenticationManager;

    public AuthenticationManager getAuthenticationManager() { return authenticationManager; }

    @Inject
    SessionBean sessionBean;

    @Override
    protected String getSubfolder() {
        return "admin";
    }

    @Override
    protected String getTitle()
    {
        return "Admin";
    }

    @PostConstruct
    protected void postConstruct() {
        authenticationManager = authenticationManagerFactory.getAuthenticationManager(request.getSession());
    }

    @Context
    HttpServletRequest request;

    @GET
    @Path("/")
    public Object index()
    {
        if (authenticationManager.isAuthenticated())
            return new View("/WEB-INF/admin/index.jsp");
        else
            return new Redirect(request.getContextPath()+"/login");
    }

    @GET
    @Path("login")
    public View getLogin() {
        request.setAttribute("action",request.getContextPath()+"/login");
        return new View(getViewRoot() + "/login.jsp");
    }

    @POST
    @Path("login")
    public Redirect postLogin(@FormParam("username") String username, @FormParam("password") String password) {
        logFine("trying login with: "+username+" "+password);
        if (authenticationManager.login(username,password)) {
            sessionBean.addMessage("logged in");
            return new Redirect(request.getContextPath() + "/");
        } else {
            sessionBean.addError("login failed");
            return new Redirect(request.getContextPath() + "/login");
        }
    }

    @POST
    @Path("logout")
    public Redirect postLogout()
    {
        authenticationManager.logout();
        sessionBean.addMessage("logged out");
        return new Redirect(request.getContextPath()+"/login");
    }

    @GET
    @Path("logout")
    public Redirect getLogout()
    {
        authenticationManager.logout();
        sessionBean.addMessage("logged out");
        return new Redirect(request.getContextPath()+"/login");
    }

}
