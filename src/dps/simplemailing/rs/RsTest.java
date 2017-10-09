package dps.simplemailing.rs;

//import org.jboss.resteasy.plugins.providers.html.View;

import dps.simplemailing.entities.Mail;
import dps.simplemailing.manage.MailManager;
import dps.simplemailing.rs.Forward;
import dps.simplemailing.rs.ResponseSent;
import org.jboss.resteasy.plugins.providers.html.View;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

@Path("test")
//@ApplicationScoped
public class RsTest {

    @Inject
    MailManager mailManager;

    @PostConstruct
    void init() {
        System.out.println("init RsTest");
    }

    @Context
    HttpServletResponse response;

    @Context
    HttpServletRequest request;

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test Success "+request.getContextPath();
    }

    @GET
    @Path("sendResponse")
    @Produces(MediaType.TEXT_PLAIN)
    public ResponseSent responseSent() throws IOException {
        response.setContentType(MediaType.TEXT_PLAIN);
        PrintWriter writer = response.getWriter();
        writer.println("Sending Response");
        return new ResponseSent();
    }

    @GET
    @Path("forwardInMethod")
    @Produces(MediaType.TEXT_PLAIN)
    public ResponseSent ownForwarding() throws IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/testview.jsp");
        try {
            requestDispatcher.forward(ResteasyProviderFactory.getContextData(HttpServletRequest.class),ResteasyProviderFactory.getContextData(HttpServletResponse.class));
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return new ResponseSent();
    }


    @GET
    @Path("sendForward")
    @Produces(MediaType.TEXT_HTML)
    public Forward showingJsp() throws IOException, ServletException {
        return new Forward("/testview.jsp");
    }

    @GET
    @Path("sendView")
    @Produces(MediaType.TEXT_HTML)
    public View getJspTest(@Context HttpServletRequest request,
                           @Context HttpServletResponse response) {
        System.out.println("running");

        return new View("/WEB-INF/testjsp.jsp");
    }

}
