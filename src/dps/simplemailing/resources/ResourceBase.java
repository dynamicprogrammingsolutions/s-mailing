package dps.simplemailing.resources;

import dps.simplemailing.entities.EntityBase;
import dps.simplemailing.manage.ManagerBase;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public List<EntityType> getAll()
    {
        return getManager().getAll();
    }


    @GET
    @Path("/")
    public List<EntityType> getRange(@QueryParam("first") int first, @QueryParam("max") int max)
    {
        return getManager().get(first,max);
    }


    @POST
    @Path("/")
    public EntityType create(EntityType mail)
    {
        getManager().create(mail);
        return mail;
    }

    @GET
    @Path("/{id}")
    public EntityType find(@PathParam("id") IdType id)
    {
        return getManager().getById(id);
    }

    @PUT
    @Path("/{id}")
    public void edit(@PathParam("id") IdType id, EntityType editedMail) {
        getManager().modify(id, editedMail);
    }

    @DELETE
    @Path("/{id}")
    public void remove(@PathParam("id") IdType id)
    {
        getManager().remove(id);
    }

}
