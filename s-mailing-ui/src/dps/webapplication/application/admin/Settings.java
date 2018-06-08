package dps.webapplication.application.admin;


import dps.commons.reflect.ReflectHelper;
import dps.webapplication.application.providers.View;
import dps.webapplication.application.providers.annotations.AllowedRoles;
import dps.webapplication.application.providers.annotations.NotAuthorizedRedirect;
import dps.webapplication.configuration.Configuration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Path("admin/settings")
@AllowedRoles("super")
public class Settings {

    @Inject
    Configuration configuration;

    @GET
    @Path("/")
    @NotAuthorizedRedirect("admin/auth/login")
    @Produces(MediaType.TEXT_HTML)
    public View index(@Context HttpServletRequest request) {
        request.setAttribute("applicationSettings",configuration.reloadConfig(dps.webapplication.configuration.Settings.class));
        return new View("/WEB-INF/admin/settings/all.jsp");
    }

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public View modify(@Context HttpServletRequest request) {

        String settingsName = request.getParameter("settings");


        Object obj = configuration.get(settingsName);
        Class<?> settingsClass = obj.getClass();

        //Class<dps.webapplication.mail.MailSettings> settingsClass = dps.webapplication.mail.MailSettings.class;
        //MailSettings obj = configuration.reloadConfig(MailSettings.class);

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            String key = param.getKey();
            String value = param.getValue()[0];
            String setterName = ReflectHelper.getSetterName(key);
            try {
                Method method = settingsClass.getMethod(setterName, String.class);
                method.invoke(obj,value);
            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new WebApplicationException(e);
            }
        }
        configuration.save(obj);
        return index(request);
    }

}
