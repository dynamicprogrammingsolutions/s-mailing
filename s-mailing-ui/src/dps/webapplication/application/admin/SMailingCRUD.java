package dps.webapplication.application.admin;

import dps.simplemailing.entities.EntityBase;
import dps.simplemailing.manage.ManagerBase;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class SMailingCRUD<EntityType extends EntityBase<IdType>, IdType> {

    abstract ManagerBase<EntityType,IdType> getManager();

    @GET
    @Path("/rest/")
    public List<EntityType> getRange(@QueryParam("first") Integer first, @QueryParam("max") Integer max)
    {
        return getManager().get(first,max);
    }

    @GET
    @Path("/rest/count")
    public Long getCount()
    {
        return getManager().count();
    }

    @GET
    @Path("/rest/{id:\\d+}")
    public EntityType find(@PathParam("id") IdType id)
    {
        try {
            return getManager().getById(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @POST
    @Path("/rest/")
    public IdType create(EntityType entity)
    {
        try {
            getManager().create(entity);
            return entity.getId();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    @PUT
    @Path("/rest/{id}")
    public void edit(@PathParam("id") IdType id, EntityType editedEntity) {
        try {
            System.out.println(editedEntity);
            getManager().modify(id, editedEntity);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    @DELETE
    @Path("/rest/{id:\\d+}")
    public void remove(@PathParam("id") IdType id)
    {
        try {
            getManager().remove(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

}
