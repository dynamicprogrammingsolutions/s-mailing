package dps.webapplication.application.admin;


import dps.webapplication.application.providers.View;
import dps.webapplication.application.providers.annotations.AllowedRoles;
import dps.webapplication.application.providers.annotations.NotAuthorizedRedirect;
import dps.webapplication.configuration.Settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("admin")
@AllowedRoles("admin")
public class AdminIndex {

    @Inject
    Settings settings;

    @Context
    HttpServletRequest request;

    @GET
    @Path("/")
    @NotAuthorizedRedirect("admin/auth/login")
    @Produces(MediaType.TEXT_HTML)
    public View index()
    {
        return new View("/WEB-INF/admin/index.jsp");
        /*try {
            return Response.temporaryRedirect(new URI(request.getAttribute("requestedHost")+settings.getRoot()+"admin/static")).build();
        } catch (URISyntaxException e) {
            throw new WebApplicationException("invalid redirection url");
        }*/
    }


}
