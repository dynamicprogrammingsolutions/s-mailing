package dps.simplemailing.admin;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "RouterServlet")
public class RouterServlet extends HttpServlet {

    final Pattern resourcePattern = Pattern.compile("^/(?:css|fonts|img|js|scss|styles)(?:/.*)?$");
    final Pattern sitePattern = Pattern.compile("^/(?:login|mails|campaigns|series|users)(?:/.*)?$");
    final Pattern indexPattern = Pattern.compile("^(?:/)?$");

    final int MAXAGE = 0;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (subService(resourcePattern,0,"/resources",req,resp,(method,response) -> {
            if ("GET".equals(method) || "HEAD".equals(method)) {
                response.addHeader("Cache-Control","max-age="+MAXAGE);
                response.addHeader("Cache-Control","public");
            }
        })) return;
        if (subService(sitePattern,0,"/application",req,resp,null)) return;
        if (subService(indexPattern,0,"/application",req,resp,null)) return;
    }

    private boolean subService(Pattern pattern, int groupIdx, String subResource, HttpServletRequest req, HttpServletResponse resp, AddHeaders addHeaders) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) pathInfo = "";
        Matcher matcher = pattern.matcher(pathInfo);
        if (matcher.find()) {
            if (addHeaders != null) {
                addHeaders.addHeaders(req.getMethod(),resp);
            }
            String forwardTo = subResource+matcher.group(groupIdx);
            RequestDispatcher dispatcher = req.getRequestDispatcher(forwardTo);
            dispatcher.forward(req,resp);
            return true;
        }
        return false;
    }

    interface AddHeaders {
        void addHeaders(String method, HttpServletResponse response);
    }

}
