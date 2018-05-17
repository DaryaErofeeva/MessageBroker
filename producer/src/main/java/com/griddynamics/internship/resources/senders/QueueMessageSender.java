package com.griddynamics.internship.resources.senders;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.entities.Consumer;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.entities.Queue;
import com.griddynamics.internship.models.entities.SourceConsumerMessage;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Component
public class QueueMessageSender {

    @Autowired
    private DAOFactory daoFactory;

    public void sendMessage(Queue queue, Message message) {

        if (getConsumer(queue, message)) message.setState("delivered");
        else message.setState("failed");

        daoFactory.getQueueDAO().updateMessageState(message);
    }


    private boolean getConsumer(Queue queue, Message message) {
        int count = 0;

        while (count < queue.getConsumers().size()) {

            Consumer consumer = queue.getConsumers().get(getConsumerIndex(queue) % queue.getConsumers().size());

            try {
                new RestTemplate().put("http://localhost:" + consumer.getPort() + "/consumer/v1/message", message, ResponseMessage.class);
                daoFactory.getQueueConsumerMessageDAO().create(
                        new SourceConsumerMessage("delivered", new Timestamp(System.currentTimeMillis()), queue, consumer, message));
                return true;

            } catch (ResourceAccessException ex) {
                daoFactory.getQueueConsumerMessageDAO().create(
                        new SourceConsumerMessage("failed", new Timestamp(System.currentTimeMillis()), queue, consumer, message));
                count++;
            }
        }
        return false;
    }

    private int getConsumerIndex(Queue queue) {
        try {
            int lastConsumerId = daoFactory.getQueueConsumerMessageDAO().getLastConsumerId(queue.getId());
            return queue.getConsumers().indexOf(daoFactory.getConsumerDAO().getEntityById(lastConsumerId)) + 1;

        } catch (EmptyResultDataAccessException ex) {
            return queue.getConsumers().size();
        }
    }
}