package dps.simplemailing.router;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "RouterServlet", urlPatterns = "/*")
public class RouterServlet extends HttpServlet {

    final Pattern apiPattern = Pattern.compile("^/api(.*)");
    final Pattern adminPattern = Pattern.compile("^/admin(.*)");
    final Pattern resourcesPattern = Pattern.compile("^/resources(.*)");
    final Pattern unsubscribePattern = Pattern.compile("^/unsubscribe(.*)");

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (processSubSite(resourcesPattern,1,"/s-mailing-resources",req,resp)) return;
        if (processSubSite(adminPattern,1,"/s-mailing-admin",req,resp)) return;
        if (processSubSite(apiPattern,1,"/s-mailing-api",req,resp)) return;
        if (processSubSite(unsubscribePattern,1,"/s-mailing-unsubscribe",req,resp)) return;
    }

    private boolean processSubSite(Pattern pattern, int patternGroup, String contextName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher = pattern.matcher(req.getPathInfo());
        System.out.println("checking pattern "+pattern);
        if (matcher.find()) {
            System.out.println("found match");
            String path = matcher.group(patternGroup);
            ServletContext context = getServletContext().getContext(contextName);
            RequestDispatcher requestDispatcher = context.getRequestDispatcher(path);
            requestDispatcher.forward(req,resp);
            return true;
        }
        return false;
    }

}
