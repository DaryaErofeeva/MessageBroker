package com.griddynamics.internship.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.griddynamics.internship.ClockService;
import com.griddynamics.internship.dao.impl.TopicDAO;
import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.Topic;
import com.griddynamics.internship.models.request.MessageRequest;
import com.griddynamics.internship.models.response.MessageResponse;
import com.griddynamics.internship.models.response.ResponseMessage;
import com.griddynamics.internship.models.response.TopicResponse;
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
public class TopicResourceTest {
    @LocalServerPort
    private int port;

    @MockBean
    private TopicDAO topicDAO;

    @MockBean
    private ClockService clockService;

    private Timestamp timestamp;

    private WebTarget webTarget;

    private ObjectMapper objectMapper;

    public TopicResourceTest() {
        timestamp = new Timestamp(System.currentTimeMillis());
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(clockService.now()).willReturn(timestamp);

        Client client = ClientBuilder.newClient();
        webTarget = client.target("http://localhost:" + port);
    }

    @Test
    public void getAllTopics() {

        given(topicDAO.getAll()).willReturn(new ArrayList<>());

        Response output = webTarget.path("/broker/v1/producer/topic").request().get();
        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
    }

    @Test
    public void getTopic() throws JsonProcessingException {
        given(topicDAO.getEntityByName("topic")).willReturn(new Topic());

        Response output = webTarget.path("/broker/v1/producer/topic/topic").request().get();
        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new TopicResponse()), output.readEntity(String.class));
    }

    @Test
    public void getTopic_EmptyResultDataAccessException() throws JsonProcessingException {
        when(topicDAO.getEntityByName("topic")).thenThrow(new EmptyResultDataAccessException(1));

        Response output = webTarget.path("/broker/v1/producer/topic/topic").request().get();
        assertEquals(400, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new ResponseMessage("No topic with such name")), output.readEntity(String.class));
    }

    @Test
    public void getMessage() throws JsonProcessingException {
        given(topicDAO.getMessageByIdAndEntityName("topic", 1)).willReturn(new Message());

        Response output = webTarget.path("/broker/v1/producer/topic/topic/1").request().get();
        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new MessageResponse()), output.readEntity(String.class));
    }

    @Test
    public void getMessage_EmptyResultDataAccessException() throws JsonProcessingException {
        when(topicDAO.getMessageByIdAndEntityName("topic", 1)).thenThrow(new EmptyResultDataAccessException(1));

        Response output = webTarget.path("/broker/v1/producer/topic/topic/1").request().get();
        assertEquals(400, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new ResponseMessage("In topic : 'topic' no message with id : '1'")), output.readEntity(String.class));
    }

    @Test
    public void createMessage() throws URISyntaxException, JsonProcessingException {
        given(topicDAO.getEntityByName("topic")).willReturn(new Topic());

        Message message = new Message();
        message.setContent("content");
        message.setState("put");
        message.setTimestamp(clockService.now());

        Response output = webTarget
                .path("/broker/v1/producer/topic/topic")
                .request()
                .put(Entity.json(new MessageRequest("content")));

        assertEquals(200, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new MessageResponse(0, "content", "put", clockService.now())), output.readEntity(String.class));
    }

    @Test
    public void createMessage_NullContent() throws JsonProcessingException {
        Response output = webTarget
                .path("/broker/v1/producer/topic/topic")
                .request()
                .put(Entity.json(new MessageRequest()));

        assertEquals(400, output.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, output.getMediaType());
        assertEquals(objectMapper.writeValueAsString(new ResponseMessage("Wrong input message format")), output.readEntity(String.class));
    }
}
