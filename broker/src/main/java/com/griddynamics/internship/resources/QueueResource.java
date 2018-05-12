package com.griddynamics.internship.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.QueueMessage;
import com.griddynamics.internship.models.request.QueueMessageRequest;
import com.griddynamics.internship.models.response.QueueResponse;
import com.griddynamics.internship.models.response.ResponseError;
import com.griddynamics.internship.models.response.plural.QueuesResponses;
import com.griddynamics.internship.resources.models.mappers.QueueMessageModelMapper;
import com.griddynamics.internship.resources.models.mappers.QueueModelMapper;
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
@JsonRootName("queue")
@Path("/broker/v1/producer/queue")
public class QueueResource {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private QueueModelMapper queueModelMapper;

    @Autowired
    private QueueMessageModelMapper queueMessageModelMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getAllQueues() {
        QueuesResponses queuesResponses = new QueuesResponses();

        List<QueueResponse> queues = new ArrayList<>();
        daoFactory.getQueueDAO()
                .getAll()
                .stream()
                .forEach(queue -> queues.add(queueModelMapper.convertToResponseObject(queue)));

        queuesResponses.setQueues(queues);

        return Response.status(200).entity(queuesResponses).build();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getQueue(@PathParam("name") String name) {
        try {
            return Response.status(200).entity(queueModelMapper.convertToResponseObject(daoFactory.getQueueDAO().getEntityByName(name))).build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseError("No queue with such name")).build();
        }
    }

    @GET
    @Path("/{name}/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getQueueMessage(@PathParam("name") String name, @PathParam("id") int id) {
        try {
            QueueMessage queueMessage = daoFactory.getQueueMessageDAO().getEntityById(id);

            if (!queueMessage.getQueue().getName().equals(name))
                return Response.status(400).entity(new ResponseError("Message with id = " + id + " belongs to another queue")).build();

            return Response.status(200).entity(queueMessageModelMapper.convertToResponseObject(queueMessage)).build();
        } catch (NullPointerException ex) {
            return Response.status(400).entity(new ResponseError("No message with such id: " + id)).build();
        }
    }


    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response updateMessage(@PathParam("name") String name, QueueMessageRequest message) throws URISyntaxException {
        if (message.getMessageId() == 0 || message.getState() == null)
            return Response.status(400).entity(new ResponseError("Wrong input message format")).entity(new QueueMessageRequest()).build();

        try {
            QueueMessage queueMessage = queueMessageModelMapper.convertToEntity(message, name);
            queueMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
            queueMessage.setId(daoFactory.getQueueMessageDAO().create(queueMessage));

            return Response
                    .status(200)
                    .entity(queueMessageModelMapper.convertToResponseObject(queueMessage))
                    .contentLocation(new URI("/broker/v1/producer/queue/" + name + "/" + queueMessage.getId()))
                    .build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseError("No queue with such name")).build();
        }
    }
}
