package dps.simplemailing.front.admin.controller;

//import org.jboss.resteasy.plugins.providers.html.View;

import javax.enterprise.context.Dependent;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Dependent
@Path("ta")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminControllerTest {

    @Path("/test_text")
    @Produces("text/html")
    @GET
    public String test()
    {
        return "TEST OK";
    }

    /*@Path("/test_html")
    @Produces(MediaType.TEXT_HTML)
    @GET
    public View testhtml()
    {
        Map<String,String> model = new HashMap<>();
        model.put("key1","value1");
        return new View("testadmin/testview.jsp",model);
    }*/

}
