package dps.simplemailing.admin.filters;

import dps.simplemailing.admin.views.RequestBean;
import dps.simplemailing.admin.views.SessionBean;
import dps.simplemailing.admin.provider.View;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@ApplicationScoped
public class SetRequestAttributesFilter implements ContainerResponseFilter {
    @Context
    HttpServletRequest request;

    @Inject
    SessionBean sessionBean;

    @Inject
    RequestBean requestBean;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object entity = responseContext.getEntity();
        if (entity instanceof View) {
            request.setAttribute("errors", sessionBean.getErrors());
            request.setAttribute("messages", sessionBean.getMessages());
            request.setAttribute("title", requestBean.getTitle());
            request.setAttribute("root", requestBean.getRoot());
            request.setAttribute("contextPath", request.getContextPath());
        }
    }
}
