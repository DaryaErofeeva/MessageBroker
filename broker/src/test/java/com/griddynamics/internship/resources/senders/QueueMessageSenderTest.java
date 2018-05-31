package com.griddynamics.internship.resources.senders;

import com.griddynamics.internship.dao.impl.ConsumerDAO;
import com.griddynamics.internship.dao.impl.QueueConsumerMessageDAO;
import com.griddynamics.internship.dao.impl.QueueDAO;
import com.griddynamics.internship.models.Consumer;
import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.Queue;
import com.griddynamics.internship.models.SourceConsumerMessage;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.client.MockRestServiceServer;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RunWith(Parameterized.class)
public class QueueMessageSenderTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private ConsumerDAO consumerDAO;

    @MockBean
    private QueueConsumerMessageDAO queueConsumerMessageDAO;

    @Autowired
    private QueueMessageSender queueMessageSender;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Queue(), 1, 1, 0, 1, "delivered", new Consumer[]{new Consumer(1, "port1", "host1"), new Consumer(2, "port2", "host2")}},
                {new Queue(), 1, 2, 1, 0, "delivered", new Consumer[]{new Consumer(1, "port1", "host1"), new Consumer(2, "port2", "host2")}},
                {new Queue(), 1, 1, 0, 0, "delivered", new Consumer[]{new Consumer(1, "port1", "host1")}},
                {new Queue(), 1, 0, 0, 0, "failed", new Consumer[]{}}
        });
    }

    private Queue queue;
    private Message message;
    private Message expectedMessage;
    private int lastConsumerId;
    private int lastConsumerIndex;
    private int calculatedConsumerIndex;

    public QueueMessageSenderTest(Queue queue, int queueId, int lastConsumerId, int lastConsumerIndex, int calculatedConsumerIndex, String expectedState, Consumer... consumers) {
        this.queue = queue;
        queue.setId(queueId);
        queue.setConsumers(Arrays.asList(consumers));

        this.message = new Message();
        this.expectedMessage = new Message();
        this.expectedMessage.setState(expectedState);

        this.lastConsumerId = lastConsumerId;
        this.lastConsumerIndex = lastConsumerIndex;
        this.calculatedConsumerIndex = calculatedConsumerIndex;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        if (queue.getConsumers().size() > 0) {
            given(queueConsumerMessageDAO.getLastConsumerId(queue.getId())).willReturn(lastConsumerId);
            given(consumerDAO.getEntityById(lastConsumerId)).willReturn(queue.getConsumers().get(lastConsumerIndex));

            Consumer consumer = queue.getConsumers().get(calculatedConsumerIndex);

            MockRestServiceServer.createServer(queueMessageSender.getRestTemplate())
                    .expect(requestTo("http://" + consumer.getHost() + "/" + consumer.getPort() + "/consumer/v1/message"))
                    .andRespond(withSuccess(String.valueOf(new ResponseMessage("Message received")), MediaType.APPLICATION_JSON));

            given(queueConsumerMessageDAO
                    .create(new SourceConsumerMessage("delivered", new Timestamp(System.currentTimeMillis()), queue, consumer, message)))
                    .willReturn(1);
        }
    }

    @Test
    public void sendMessage() {
        queueMessageSender.sendMessage(queue, message);
        verify(queueDAO, times(1)).updateMessageState(expectedMessage);
    }
}
