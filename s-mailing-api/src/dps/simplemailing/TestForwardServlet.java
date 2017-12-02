package dps.simplemailing;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TestForwardServlet", urlPatterns = "/testforward/*")
public class TestForwardServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        ServletContext web1 = getServletContext();
        ServletContext web2 = web1.getContext("/s-mailing/admin");
        System.out.println(web2+" "+request.getPathInfo());
        RequestDispatcher dispatcher = web2.getRequestDispatcher(request.getPathInfo());
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
}
