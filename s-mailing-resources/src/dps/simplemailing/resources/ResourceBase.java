package dps.simplemailing.resources;

import dps.simplemailing.entities.EntityBase;
import dps.simplemailing.manage.ManagerBase;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class ResourceBase<EntityType extends EntityBase<IdType>,IdType> {

    @Inject
    ManagerBase<EntityType,IdType> manager;

    ManagerBase<EntityType,IdType> getManager()
    {
        return manager;
    }

    @GET
    @Path("/")
    public List<EntityType> get(@QueryParam("first") Integer first, @QueryParam("max") Integer max)
    {
        if (first == null || max == null)
            return getManager().getAll();
        else
            return getManager().get(first,max);
    }

    @GET
    @Path("/count")
    public JsonObject count()
    {
        return Json.createObjectBuilder().add("count",getManager().count()).build();
    }


    @POST
    @Path("/")
    public EntityType create(EntityType mail)
    {
        getManager().create(mail);
        return mail;
    }

    @GET
    @Path("/{id:\\d+}")
    public EntityType find(@PathParam("id") IdType id)
    {
        return getManager().getById(id);
    }

    @PUT
    @Path("/{id:\\d+}")
    public void edit(@PathParam("id") IdType id, EntityType editedMail) {
        getManager().modify(id, editedMail);
    }

    @DELETE
    @Path("/{id:\\d+}")
    public void remove(@PathParam("id") IdType id)
    {
        getManager().remove(id);
    }

}
