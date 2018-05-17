package com.griddynamics.internship;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

@Controller
@Path("/consumer/v1")
public class ConsumerResource {

    @PUT
    @Path("/message")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Response createMessage(MessageRequest messageRequest) throws URISyntaxException {
        System.out.println("New message '" + messageRequest + "'");
        return Response.status(200).entity(new ResponseMessage("Message received")).build();
    }
}
