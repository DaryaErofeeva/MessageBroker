package com.griddynamics.internship.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.TopicMessage;
import com.griddynamics.internship.models.request.TopicMessageRequest;
import com.griddynamics.internship.models.response.ResponseError;
import com.griddynamics.internship.models.response.TopicResponse;
import com.griddynamics.internship.models.response.plural.TopicsResponses;
import com.griddynamics.internship.resources.models.mappers.TopicMessageModelMapper;
import com.griddynamics.internship.resources.models.mappers.TopicModelMapper;
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
import java.util.List;

@Controller
@JsonRootName("topic")
@Path("/broker/v1/producer/topic")
public class TopicResource {
    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private TopicModelMapper topicModelMapper;

    @Autowired
    private TopicMessageModelMapper topicMessageModelMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getAllTopics() {
        TopicsResponses topicsResponses = new TopicsResponses();

        List<TopicResponse> topics = new ArrayList<>();
        daoFactory.getTopicDAO()
                .getAll()
                .stream()
                .forEach(topic -> topics.add(topicModelMapper.convertToResponseObject(topic)));

        topicsResponses.setTopics(topics);

        return Response.status(200).entity(topicsResponses).build();
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
    public Response getQueueMessage(@PathParam("name") String name, @PathParam("id") int id) {
        try {
            TopicMessage topicMessage = daoFactory.getTopicMessageDAO().getEntityById(id);

            if (!topicMessage.getTopicConsumer().getTopic().getName().equals(name))
                return Response.status(400).entity(new ResponseError("Message with id = " + id + " belongs to another topic")).build();

            return Response.status(200).entity(topicMessageModelMapper.convertToResponseObject(topicMessage)).build();
        } catch (NullPointerException ex) {
            return Response.status(400).entity(new ResponseError("No message with such id: " + id)).build();
        }
    }


    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response updateMessage(@PathParam("name") String name, TopicMessageRequest message) throws URISyntaxException {

        if (message.getMessageId() == 0 || message.getState() == null || message.getConsumerId() == 0)
            return Response.status(400).entity(new ResponseError("Wrong input message format")).entity(new TopicMessageRequest()).build();

        try {
            TopicMessage topicMessage = topicMessageModelMapper.convertToEntity(message, name);
            topicMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
            topicMessage.setId(daoFactory.getTopicMessageDAO().create(topicMessage));

            return Response
                    .status(200)
                    .entity(topicMessageModelMapper.convertToResponseObject(topicMessage))
                    .contentLocation(new URI("/broker/v1/producer/topic/" + name + "/" + topicMessage.getId()))
                    .build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseError("No queue with such name")).build();
        }
    }
}
