package dps.simplemailing.admin.filters;

import dps.simplemailing.admin.provider.Markdown;
import dps.simplemailing.admin.provider.View;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@ApplicationScoped
@Priority(Priorities.ENTITY_CODER+200)
public class MarkdownFilter implements ContainerResponseFilter {

    static final Parser PARSER = Parser.builder().build();
    static final HtmlRenderer RENDERER = HtmlRenderer.builder().build();

    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object entity = responseContext.getEntity();
        if (entity instanceof Markdown) {
            Markdown md = (Markdown)entity;

            Node document = PARSER.parse(md.getMd());
            String html = RENDERER.render(document);

            request.setAttribute("pageContents",html);

            responseContext.setEntity(new View("/WEB-INF/pages/page.jsp"));
        }
    }
}
