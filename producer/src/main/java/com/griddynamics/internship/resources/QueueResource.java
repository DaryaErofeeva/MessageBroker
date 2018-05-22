package com.griddynamics.internship.resources;

import com.griddynamics.internship.ClockService;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.resources.senders.QueueMessageSender;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.entities.Queue;
import com.griddynamics.internship.models.request.MessageRequest;
import com.griddynamics.internship.models.response.ResponseMessage;
import com.griddynamics.internship.models.response.plural.QueuesResponse;
import com.griddynamics.internship.resources.model.mappers.MessageModelMapper;
import com.griddynamics.internship.resources.model.mappers.QueueModelMapper;
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
@Path("/broker/v1/producer/queue")
public class QueueResource {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private QueueModelMapper queueModelMapper;

    @Autowired
    private MessageModelMapper messageModelMapper;

    @Autowired
    private QueueMessageSender queueMessageSender;

    @Autowired
    private ClockService clockService;

    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getAllQueues() {
        QueuesResponse queuesResponses = new QueuesResponse();
        queuesResponses.setQueues(new ArrayList<>());
        daoFactory.getQueueDAO()
                .getAll()
                .forEach(queue -> queuesResponses.getQueues().add(queueModelMapper.convertToResponseObject(queue)));
        return Response.status(200).entity(queuesResponses).build();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getQueue(@PathParam("name") String name) {
        try {
            return Response.status(200).entity(queueModelMapper.convertToResponseObject(daoFactory.getQueueDAO().getEntityByName(name))).build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseMessage("No queue with such name")).build();
        }
    }

    @GET
    @Path("/{name}/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response getMessage(@PathParam("name") String name, @PathParam("id") int id) {
        try {
            return Response.status(200).entity(messageModelMapper.convertToResponseObject(daoFactory.getQueueDAO().getMessageByIdAndEntityName(name, id))).build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseMessage("In queue : '" + name + "' no message with id : '" + id + "'")).build();
        }
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response createMessage(@PathParam("name") String name, MessageRequest messageRequest) throws URISyntaxException {

        if (messageRequest.getContent() == null)
            return Response.status(400).entity(new Object[]{new ResponseMessage("Wrong input message format"), new MessageRequest()}).build();

        try {
            Message message = messageModelMapper.convertToEntity(messageRequest);
            message.setState("put");
            message.setTimestamp(clockService.now());

            Queue queue = daoFactory.getQueueDAO().getEntityByName(name);
            daoFactory.getQueueDAO().createMessage(queue, message);

            if (queue.getConsumers().size() > 0)
                queueMessageSender.sendMessage(queue, message);

            return Response
                    .status(200)
                    .entity(messageModelMapper.convertToResponseObject(message))
                    .contentLocation(new URI("/broker/v1/producer/queue/" + name + "/" + message.getId()))
                    .build();
        } catch (EmptyResultDataAccessException ex) {
            return Response.status(400).entity(new ResponseMessage("No queue with such name")).build();
        }
    }
}
