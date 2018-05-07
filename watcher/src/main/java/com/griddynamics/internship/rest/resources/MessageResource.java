package com.griddynamics.internship.rest.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.plural.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@JsonRootName("messages")
@Path("/broker/v1/messages")
public class MessageResource {

    @Autowired
    private DAOFactory daoFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Messages getAllMessages() {
        Messages messages = new Messages();
        messages.setMessages(daoFactory.getMessagesDAO().getAll());
        return messages;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createMessage(Message message) {
        try {
            daoFactory.getMessagesDAO().createEntityIfNotExists(message);
            return Response.status(201).entity(message).build();
        } catch (NullPointerException e) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createMessage(@FormParam("channel_id") int channelId, @FormParam("name") String name) {
        return createMessage(new Message(daoFactory.getChannelsDAO().getEntityById(channelId), name));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessageById(@PathParam("id") int id) {
        try {
            return Response
                    .status(200)
                    .entity(daoFactory.getMessagesDAO().getEntityById(id))
                    .build();
        } catch (EmptyResultDataAccessException e) {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateMessage(@PathParam("id") int id, Message message) {
        try {
            daoFactory.getMessagesDAO().update(message);
            return Response.status(200).entity(message).build();
        } catch (NullPointerException e) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateMessage(@PathParam("id") int id, @FormParam("0") int channelId, @FormParam("0") String name) {
        return updateMessage(id, new Message(daoFactory.getChannelsDAO().getEntityById(channelId), name));
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") int id) {
        if (daoFactory.getMessagesDAO().delete(id) == 1)
            return Response.status(200).build();
        return Response.status(400).entity("Please provide right id").build();
    }
}
