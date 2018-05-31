package com.griddynamics.internship.resources.senders;

import com.griddynamics.internship.ClockService;
import com.griddynamics.internship.dao.impl.TopicConsumerMessageDAO;
import com.griddynamics.internship.dao.impl.TopicDAO;
import com.griddynamics.internship.models.Consumer;
import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.SourceConsumerMessage;
import com.griddynamics.internship.models.Topic;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(Parameterized.class)
public class TopicMessageSenderTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private TopicDAO topicDAO;

    @MockBean
    private TopicConsumerMessageDAO topicConsumerMessageDAO;

    @MockBean
    private ClockService clockService;

    @Autowired
    private TopicMessageSender topicMessageSender;

    @Parameterized.Parameters
    public static Collection<Object[]> dataFirst() {
        return Arrays.asList(new Object[][]{
                {new Consumer("host1", "port1"), new Message(), "delivered",
                        new Topic(), new Consumer[]{new Consumer("host1", "port1"), new Consumer("host2", "port2")}},
                {new Consumer("host1", "port1"), new Message(), "failed",
                        new Topic(), new Consumer[]{}}
        });
    }

    private Consumer consumer;
    private Message message;
    private Message expectedMessage;
    private Topic topic;
    private String expectedState;

    public TopicMessageSenderTest(Consumer consumer, Message message, String expectedState, Topic topic, Consumer... consumers) {
        this.consumer = consumer;
        this.message = message;
        this.expectedState = expectedState;

        this.topic = topic;
        this.topic.setConsumers(Arrays.asList(consumers));

        this.expectedMessage = new Message();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(restTemplateBuilder.build()).willReturn(restTemplate);
        given(clockService.now()).willReturn(new Timestamp(System.currentTimeMillis()));

        this.message.setState("put");
    }

    @Test
    public void sendMessageToConsumer() {
        topicMessageSender.sendMessage(consumer, message);

        verify(restTemplate, times(1))
                .put("http://" + consumer.getHost() + ":" + consumer.getPort() + "/consumer/v1/message", message, ResponseMessage.class);
    }

    @Test
    public void sendMessageToConsumers() {
        topicMessageSender.sendMessage(topic, message);

        topic.getConsumers().forEach(topicConsumer -> {
            verify(restTemplate, times(1))
                    .put("http://" + topicConsumer.getHost() + ":" + topicConsumer.getPort() + "/consumer/v1/message", message, ResponseMessage.class);

            verify(topicConsumerMessageDAO, times(1))
                    .create(new SourceConsumerMessage("delivered", clockService.now(), topic, topicConsumer, message));
        });

        expectedMessage.setState(expectedState);
        verify(topicDAO, times(1)).updateMessageState(expectedMessage);
    }

    @Test
    public void sendMessageToConsumers_ResourceAccessException() {
        topic.getConsumers().forEach(topicConsumer ->
                doThrow(new ResourceAccessException(""))
                        .when(restTemplate)
                        .put("http://" + topicConsumer.getHost() + ":" + topicConsumer.getPort() + "/consumer/v1/message", message, ResponseMessage.class));

        topicMessageSender.sendMessage(topic, message);

        expectedMessage.setState("failed");
        verify(topicDAO, times(1)).updateMessageState(expectedMessage);
    }
}
