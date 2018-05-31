package com.griddynamics.internship.resources;

import com.griddynamics.internship.dao.impl.ConsumerDAO;
import com.griddynamics.internship.dao.impl.QueueDAO;
import com.griddynamics.internship.dao.impl.TopicDAO;
import com.griddynamics.internship.models.Consumer;
import com.griddynamics.internship.models.Queue;
import com.griddynamics.internship.models.Topic;
import com.griddynamics.internship.models.request.ConsumerRequest;
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
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(Parameterized.class)
public class ConsumerResourceTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @MockBean
    private ConsumerDAO consumerDAO;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private TopicDAO topicDAO;

    @Autowired
    private ConsumerResource consumerResource;

    @Parameterized.Parameters
    public static Collection<Object[]> dataFirst() {
        return Arrays.asList(new Object[][]{
                {1, "host", "port", "queue", "", 200},
                {1, "host", "port", "", "topic", 200},
                {1, "host", "", "", "", 400}
        });
    }

    private int id;
    private ConsumerRequest consumerRequest;
    private Consumer consumer;
    private Queue queue;
    private Topic topic;
    private int expectedStatus;

    public ConsumerResourceTest(int id, String host, String port, String queueName, String topicName, int expectedStatus) {

        this.id = id;

        this.consumerRequest = new ConsumerRequest();
        this.consumerRequest.setHost(host);
        this.consumerRequest.setPort(port);
        this.consumerRequest.setQueue(queueName);
        this.consumerRequest.setTopic(topicName);

        this.consumer = new Consumer();
        this.consumer.setHost(host);
        this.consumer.setPort(port);

        this.queue = new Queue();
        queue.setName(queueName);

        this.topic = new Topic();
        topic.setName(topicName);

        this.expectedStatus = expectedStatus;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(consumerDAO.create(consumer)).then(answer -> {
            consumer.setId(id);
            return id;
        });

        given(queueDAO.getEntityByName(consumerRequest.getQueue())).willReturn(queue);
        given(queueDAO.addConsumer(queue, consumer)).willReturn(id);

        given(topicDAO.getEntityByName(consumerRequest.getTopic())).willReturn(topic);
        given(topicDAO.addConsumer(topic, consumer)).willReturn(id);
    }

    @Test
    public void createConsumer() {
        assertEquals(expectedStatus, consumerResource.createConsumer(consumerRequest).getStatus());
    }
}
