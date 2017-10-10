package dps.simplemailing.admin.filters;

import dps.simplemailing.admin.views.RequestBean;
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
public class TemplateFilter implements ContainerResponseFilter {

    @Inject
    RequestBean requestBean;

    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object entity = responseContext.getEntity();
        if (entity instanceof View) {
            View view = (View)entity;
            request.setAttribute("contents",view.getView());
            String template = requestBean.getTemplate() != null ? requestBean.getTemplate() : "/WEB-INF/templates/template.jsp";
            responseContext.setEntity(new View(template));
        }
    }
}
