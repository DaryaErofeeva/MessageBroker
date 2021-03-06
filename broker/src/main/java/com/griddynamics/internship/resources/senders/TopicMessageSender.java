package com.griddynamics.internship.resources.senders;

import com.griddynamics.internship.ClockService;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Consumer;
import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.SourceConsumerMessage;
import com.griddynamics.internship.models.Topic;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicMessageSender {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private ClockService clockService;

    public void sendMessage(Topic topic, Message message) {

        List<SourceConsumerMessage> consumerMessages = new ArrayList<>();

        for (Consumer consumer : topic.getConsumers()) {
            if (sendMessage(consumer, message)) {
                consumerMessages.add(new SourceConsumerMessage("delivered", clockService.now(), topic, consumer, message));
                message.setState("delivered");
            } else
                consumerMessages.add(new SourceConsumerMessage("failed", clockService.now(), topic, consumer, message));
        }

        if (!message.getState().equals("delivered"))
            message.setState("failed");

        if (message.getState().equals("delivered"))
            consumerMessages.forEach(consumerMessage -> daoFactory.getTopicConsumerMessageDAO().create(consumerMessage));

        daoFactory.getTopicDAO().updateMessageState(message);
    }

    public boolean sendMessage(Consumer consumer, Message message) {
        try {
            restTemplateBuilder.build().put("http://" + consumer.getHost() + ":" + consumer.getPort() + "/consumer/v1/message", message, ResponseMessage.class);
            return true;
        } catch (ResourceAccessException ex) {
            return false;
        }
    }
}
