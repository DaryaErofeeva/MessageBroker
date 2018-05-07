package com.griddynamics.internship.rest.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Change;
import com.griddynamics.internship.models.plural.Changes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

@Controller
@JsonRootName("changes")
@Path("/broker/v1/changes")
public class ChangeResource {

    @Autowired
    private DAOFactory daoFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Changes getAllChanges() {
        Changes changes = new Changes();
        changes.setChanges(daoFactory.getChangesDAO().getAll());
        return changes;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createChange(Change change) {
        try {
            daoFactory.getChangesDAO().createEntityIfNotExists(change);
            return Response
                    .status(201)
                    .entity(change)
                    .build();
        } catch (NullPointerException e) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createChange(@FormParam("message_id") int messageId, @FormParam("content") String content) {
        return createChange(new Change(
                daoFactory.getMessagesDAO().getEntityById(messageId),
                content,
                new Timestamp(System.currentTimeMillis())));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChangeById(@PathParam("id") int id) {
        try {
            return Response
                    .status(200)
                    .entity(daoFactory.getChangesDAO().getEntityById(id))
                    .build();
        } catch (EmptyResultDataAccessException e) {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateChange(@PathParam("id") int id, Change change) {
        try {
            daoFactory.getChangesDAO().update(change);
            return Response.status(200).entity(change).build();
        } catch (NullPointerException e) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateChange(@PathParam("id") int id, @FormParam("message_id") int messageId, @FormParam("content") String content) {
        return updateChange(
                id,
                new Change(
                        daoFactory.getMessagesDAO().getEntityById(messageId),
                        content,
                        new Timestamp(System.currentTimeMillis())));
    }

    @DELETE
    @Path("/{id}")
    public Response deleteChange(@PathParam("id") int id) {
        if (daoFactory.getChangesDAO().delete(id) == 1)
            return Response.status(200).build();
        return Response.status(400).entity("Please provide right id").build();
    }
}
