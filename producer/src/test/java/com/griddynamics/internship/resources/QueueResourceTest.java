package com.griddynamics.internship.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.internship.ClockService;
import com.griddynamics.internship.dao.impl.QueueDAO;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.entities.Queue;
import com.griddynamics.internship.models.request.MessageRequest;
import com.griddynamics.internship.models.response.MessageResponse;
import com.griddynamics.internship.models.response.QueueResponse;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class QueueResourceTest {

    @LocalServerPort
    private int port;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private ClockService clockService;

    private Timestamp timestamp;

    private WebTarget webTarget;

    private ObjectMapper objectMapper;

    public QueueResourceTest() {
        timestamp = new Timestamp(System.currentTimeMillis());
        objectMapper = new ObjectMapper();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(clockService.now()).willReturn(timestamp);

        Client client = ClientBuilder.newClient();
        webTarget = client.target("http://localhost:" + port);
    }

    @Test
    public void getAllQueues() {

        given(queueDAO.getAll()).willReturn(new ArrayList<>());

        Response output = webTarget.path("/broker/v1/producer/queue").request().get();
        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
    }

    @Test
    public void getQueue() throws JsonProcessingException {
        given(queueDAO.getEntityByName("queue")).willReturn(new Queue());

        Response output = webTarget.path("/broker/v1/producer/queue/queue").request().get();
        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new QueueResponse()), output.readEntity(String.class));
    }

    @Test
    public void getQueue_EmptyResultDataAccessException() throws JsonProcessingException {

        when(queueDAO.getEntityByName("queue")).thenThrow(new EmptyResultDataAccessException(1));

        Response output = webTarget.path("/broker/v1/producer/queue/queue").request().get();
        assertEquals(400, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new ResponseMessage("No queue with such name")), output.readEntity(String.class));
    }

    @Test
    public void getMessage() throws JsonProcessingException {
        given(queueDAO.getMessageByIdAndEntityName("queue", 1)).willReturn(new Message());

        Response output = webTarget.path("/broker/v1/producer/queue/queue/1").request().get();
        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new MessageResponse()), output.readEntity(String.class));
    }

    @Test
    public void getMessage_EmptyResultDataAccessException() throws JsonProcessingException {
        when(queueDAO.getMessageByIdAndEntityName("queue", 1)).thenThrow(new EmptyResultDataAccessException(1));

        Response output = webTarget.path("/broker/v1/producer/queue/queue/1").request().get();
        assertEquals(400, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new ResponseMessage("In queue : 'queue' no message with id : '1'")), output.readEntity(String.class));
    }

    @Test
    public void createMessage() throws URISyntaxException, JsonProcessingException {
        given(queueDAO.getEntityByName("queue")).willReturn(new Queue());

        Message message = new Message();
        message.setContent("content");
        message.setState("put");
        message.setTimestamp(clockService.now());

        Response output = webTarget
                .path("/broker/v1/producer/queue/queue")
                .request()
                .put(Entity.json(new MessageRequest("content")));

        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new MessageResponse(0, "content", "put", clockService.now())), output.readEntity(String.class));
    }

    @Test
    public void createMessage_NullContent() throws JsonProcessingException {
        Response output = webTarget
                .path("/broker/v1/producer/queue/queue")
                .request()
                .put(Entity.json(new MessageRequest()));

        assertEquals(400, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new ResponseMessage("Wrong input message format")), output.readEntity(String.class));
    }

    @Test
    public void createMessage_EmptyResultDataAccess() throws JsonProcessingException {
        given(queueDAO.getEntityByName("queue")).willThrow(new EmptyResultDataAccessException(1));

        Response output = webTarget
                .path("/broker/v1/producer/queue/queue")
                .request()
                .put(Entity.json(new MessageRequest("content")));

        assertEquals(400, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new ResponseMessage("No queue with such name")), output.readEntity(String.class));
    }
}
