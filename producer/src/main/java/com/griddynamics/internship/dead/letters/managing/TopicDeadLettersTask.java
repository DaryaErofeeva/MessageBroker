package com.griddynamics.internship.dead.letters.managing;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.resources.senders.TopicMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TopicDeadLettersTask {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private TopicMessageSender topicMessageSender;

    @Scheduled(fixedRate = 50000)
    private void sendDeadMessage() {
        daoFactory.getTopicDAO().getAllFailed()
                .forEach(topic -> topic.getMessages().forEach(
                        message -> {
                            System.out.println("Trying to send dead topic-letter: " + message);
                            topicMessageSender.sendMessage(topic, message);
                        }));
    }

    @Scheduled(fixedRate = 50000)
    private void sendDeadMessageConsumers() {
        daoFactory.getTopicConsumerMessageDAO().getFailed()
                .forEach(failedMessage -> {
                    System.out.println("Trying to send dead topic-letter: '" + failedMessage.getMessage() + "' to consumer "+failedMessage.getConsumer());
                    if (topicMessageSender.sendMessage(failedMessage.getConsumer(), failedMessage.getMessage())) {
                        failedMessage.setState("delivered");
                        daoFactory.getTopicConsumerMessageDAO().update(failedMessage);
                    }
                });
    }
}
