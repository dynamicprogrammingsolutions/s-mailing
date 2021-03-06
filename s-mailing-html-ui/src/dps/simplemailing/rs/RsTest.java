package dps.simplemailing.rs;

//import org.jboss.resteasy.plugins.providers.html.View;

import dps.simplemailing.manage.MailManager;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;

//@Path("test")
//@ApplicationScoped
public class RsTest {

    @Inject
    MailManager mailManager;

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

}
