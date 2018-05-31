package com.griddynamics.internship.resources;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.dao.SourceDAO;
import com.griddynamics.internship.models.Consumer;
import com.griddynamics.internship.models.request.ConsumerRequest;
import com.griddynamics.internship.models.response.ResponseMessage;
import com.griddynamics.internship.resources.model.mappers.ConsumerModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Controller
@Path("/broker/v1/consumer")
public class ConsumerResource {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private ConsumerModelMapper consumerModelMapper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response createConsumer(ConsumerRequest consumerRequest) {
        Consumer consumer = consumerModelMapper.convertToEntity(consumerRequest);
        daoFactory.getConsumerDAO().create(consumer);

        if (consumerRequest.getQueue() != null && !consumerRequest.getQueue().equals(""))
            addConsumer(daoFactory.getQueueDAO(), consumerRequest.getQueue(), consumer);
        else if (consumerRequest.getTopic() != null && !consumerRequest.getTopic().equals(""))
            addConsumer(daoFactory.getTopicDAO(), consumerRequest.getTopic(), consumer);
        else return Response.status(400).entity(new ResponseMessage("No queue or topic to subscript to")).build();

        return Response.status(200).entity(consumer).build();
    }

    private void addConsumer(SourceDAO sourceDAO, String sourceName, Consumer consumer) {
        sourceDAO.merge(sourceName);
        sourceDAO.addConsumer(sourceDAO.getEntityByName(sourceName), consumer);
    }
}
