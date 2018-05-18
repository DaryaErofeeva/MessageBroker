package com.griddynamics.internship.dead.letters.managing;

import com.griddynamics.internship.LogService;
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

    @Autowired
    private LogService logService;

    @Scheduled(fixedRate = 50000)
    private void sendDeadMessage() {
        daoFactory.getTopicDAO().getAllFailed()
                .forEach(topic -> topic.getMessages().forEach(
                        message -> {
                            logService.log("Trying to send dead topic-letter: {}", message);
                            topicMessageSender.sendMessage(topic, message);
                        }));
    }

    @Scheduled(fixedRate = 50000)
    private void sendDeadMessageConsumers() {
        daoFactory.getTopicConsumerMessageDAO().getFailed()
                .forEach(failedMessage -> {
                    logService.log("Trying to send dead topic-letter: '{}' to consumer {}", failedMessage.getMessage(), failedMessage.getConsumer());
                    if (topicMessageSender.sendMessage(failedMessage.getConsumer(), failedMessage.getMessage())) {
                        failedMessage.setState("delivered");
                        daoFactory.getTopicConsumerMessageDAO().update(failedMessage);
                    }
                });
    }
}
