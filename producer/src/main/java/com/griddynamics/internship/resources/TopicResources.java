package com.griddynamics.internship.resources;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.request.MessageRequest;
import com.griddynamics.internship.models.response.ResponseError;
import com.griddynamics.internship.models.response.plural.TopicsResponse;
import com.griddynamics.internship.resources.model.mappers.MessageModelMapper;
import com.griddynamics.internship.resources.model.mappers.TopicModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;

@Controller
@Path("/broker/v1/producer/topic")
public class TopicResources {
    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private TopicModelMapper topicModelMapper;

    @Autowired
    private MessageModelMapper messageModelMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getAllTopics() {
        TopicsResponse topicsResponse = new TopicsResponse();
        topicsResponse.setTopics(new ArrayList<>());
        daoFactory.getTopicDAO()
                .getAll()
                .forEach(topic -> topicsResponse.getTopics().add(topicModelMapper.convertToResponseObject(topic)));
        return Response.status(200).entity(topicsResponse).build();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getTopic(@PathParam("name") String name) {
        try {
            return Response.status(200).entity(topicModelMapper.convertToResponseObject(daoFactory.getTopicDAO().getEntityByName(name))).build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseError("No topic with such name")).build();
        }
    }

    @GET
    @Path("/{name}/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getMessage(@PathParam("name") String name, @PathParam("id") int id) {
        try {
            return Response.status(200).entity(messageModelMapper.convertToResponseObject(daoFactory.getTopicDAO().getMessageByIdAndEntityName(name, id))).build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseError("In topic : '" + name + "' no message with id : '" + id + "'")).build();
        }
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response createMessage(@PathParam("name") String name, MessageRequest messageRequest) throws URISyntaxException {

        if (messageRequest.getContent() == null || messageRequest.getState() == null)
            return Response.status(400).entity(new Object[]{new ResponseError("Wrong input message format"), new MessageRequest()}).build();

        try {
            Message message = messageModelMapper.convertToEntity(messageRequest);
            message.setTimestamp(new Timestamp(System.currentTimeMillis()));
            daoFactory.getTopicDAO().createMessage(daoFactory.getTopicDAO().getEntityByName(name), message);

            return Response
                    .status(200)
                    .entity(messageModelMapper.convertToResponseObject(message))
                    .contentLocation(new URI("/broker/v1/producer/topic/" + name + "/" + message.getId()))
                    .build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseError("No topic with such name")).build();
        }
    }
}