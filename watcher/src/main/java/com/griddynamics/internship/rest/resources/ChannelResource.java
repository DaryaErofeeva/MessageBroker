package com.griddynamics.internship.rest.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Channel;
import com.griddynamics.internship.models.plural.Channels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@JsonRootName("channels")
@Path("/broker/v1/channels")
public class ChannelResource {

    @Autowired
    private DAOFactory daoFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Channels getAllChannels() {
        Channels channels = new Channels();
        channels.setChannels(daoFactory.getChannelsDAO().getAll());
        return channels;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createChannel(Channel channel) {
        try {
            daoFactory.getChannelsDAO().createEntityIfNotExists(channel);
            return Response.status(201).entity(channel).build();
        } catch (NullPointerException e) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createChannel(@FormParam("path") String path, @FormParam("name") String name) {
        return createChannel(new Channel(path, name));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChannelById(@PathParam("id") int id) {
        try {
            return Response
                    .status(200)
                    .entity(daoFactory.getChannelsDAO().getEntityById(id))
                    .build();
        } catch (EmptyResultDataAccessException e) {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateChannel(@PathParam("id") int id, Channel channel) {
        try {
            daoFactory.getChannelsDAO().update(channel);
            return Response.status(200).entity(channel).build();
        } catch (NullPointerException e) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateChannel(@PathParam("id") int id, @FormParam("path") String path, @FormParam("name") String name) {
        return updateChannel(id, new Channel(path, name));
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") int id) {
        if (daoFactory.getChannelsDAO().delete(id) == 1)
            return Response.status(200).build();
        return Response.status(400).entity("Please provide right id").build();
    }
}
