package com.griddynamics.internship.resources;

import com.griddynamics.internship.LogService;
import com.griddynamics.internship.models.request.MessageRequest;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

@Controller
@Path("/consumer/v1")
public class ConsumerResource {

    @Autowired
    private LogService logService;

    @PUT
    @Path("/message")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response createMessage(MessageRequest messageRequest) throws URISyntaxException {
        logService.log("New message '{}'", messageRequest);
        return Response.status(200).entity(new ResponseMessage("Message received")).build();
    }
}
