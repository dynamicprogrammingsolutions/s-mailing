package dps.webapplication.application.admin;

import dps.webapplication.application.providers.Redirect;
import dps.webapplication.application.providers.View;
import dps.webapplication.authentication.CurrentAuthenticationManager;
import dps.webapplication.configuration.Settings;
import dps.webapplication.messages.Messages;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("admin/auth")
public class Authentication {

    @Inject
    Settings settings;

    @Inject
    CurrentAuthenticationManager authenticationManager;

    @Context
    HttpServletRequest request;

    @Inject
    Messages messages;


    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public View login()
    {
        return new View("/WEB-INF/admin/login.jsp");
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Redirect processLogin(@FormParam("username") String username, @FormParam("password") String password)
    {
        if (authenticationManager.login(username,password)) {
            messages.addMessage(Messages.Type.Success,"Sikeres bejelentkezés");
            Object originalRequest = request.getSession().getAttribute("originalRequest");
            Object originalRequestHash = request.getSession().getAttribute("originalRequestHash");
            if (originalRequest != null && originalRequest instanceof String) {
                if (originalRequestHash != null && originalRequestHash instanceof String) {
                    originalRequest = (String)originalRequest + (String)originalRequestHash;
                }
                return new Redirect((String)originalRequest);
            } else {
                return new Redirect(request.getAttribute("requestedHost") + settings.getRoot() + "admin/static");
            }
        } else {
            messages.addMessage(Messages.Type.Error,"A megadott jelszó vagy felhasználónév hibás");
            return new Redirect(request.getAttribute("requestedHost")+settings.getRoot()+"admin/auth/login");
        }
    }

    @POST
    @Path("/login/hash")
    @Consumes(MediaType.TEXT_PLAIN)
    public void hash(String hash)
    {
        request.getSession().setAttribute("originalRequestHash",hash);
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public void processLogin(Credentials credentials)
    {
        if (authenticationManager.login(credentials.getUsername(),credentials.getPassword())) {
            return;
        } else {
            throw new NotAuthorizedException("login credentials not correct");
        }
    }


    public static class Credentials {
        private String username;
        private String password;

        public Credentials() {}

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @GET
    @Path("/logout")
    public Redirect logout()
    {
        authenticationManager.logout();
        return new Redirect(request.getAttribute("requestedHost")+settings.getRoot()+"admin");
    }

}
