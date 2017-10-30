package dps.simplemailing.admin.controllers;

import dps.logging.HasLogger;
import dps.simplemailing.admin.authentication.UsingAuthenticationManager;
import dps.simplemailing.admin.interceptors.AuthenticationInterceptor;
import dps.simplemailing.admin.interceptors.RunInitMethod;
import dps.simplemailing.admin.provider.Markdown;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Path("pages")
@ApplicationScoped
@Interceptors({RunInitMethod.class, AuthenticationInterceptor.class})
public class PagesController extends AdminControllerBase implements ControllerInit, UsingAuthenticationManager, HasLogger {

    @Override
    protected String getSubfolder() {
        return "pages";
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Context
    HttpServletRequest request;

    @GET
    @Path("/{page:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Markdown page(@PathParam("page") String page)
    {
        String[] parts = page.split("/");
        String name = parts[parts.length-1];
        String title = name.replaceAll("_"," ");
        requestBean.setTitle(title);
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("pages/"+page+".md");
        if (resourceStream == null)
            throw new NotFoundException();
        String mdContent = new BufferedReader(new InputStreamReader(resourceStream)).lines().collect(Collectors.joining("\n"));
        return new Markdown(mdContent);
    }

}
