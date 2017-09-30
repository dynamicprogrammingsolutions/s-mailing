package dps.simplemailing.resources;

import org.jboss.resteasy.plugins.providers.html.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;

@Path("test_resource")
public class ResourceTest {

    @Context
    HttpServletRequest request;

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() throws ServletException, IOException
    {
        return "Test Success "+request.getContextPath();
    }


    @GET
    @Path("testjsp")
    @Produces(MediaType.TEXT_HTML)
    public View getJspTest(@Context HttpServletRequest request,
                           @Context HttpServletResponse response) throws ServletException, IOException {
        System.out.println("running");

        return new View("/WEB-INF/testjsp.jsp");
    }

}
