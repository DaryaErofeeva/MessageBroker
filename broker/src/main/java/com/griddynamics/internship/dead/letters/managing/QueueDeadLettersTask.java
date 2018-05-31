package com.griddynamics.internship.dead.letters.managing;

import com.griddynamics.internship.LogService;
import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.resources.senders.QueueMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueueDeadLettersTask {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private QueueMessageSender queueMessageSender;

    @Autowired
    private LogService logService;

    @Scheduled(fixedRate = 50000)
    private void sendDeadMessage() {
        daoFactory.getQueueDAO().getAllFailed()
                .forEach(queue -> queue.getMessages().forEach(
                        message -> {
                            logService.log("Trying to send dead queue-letter: {}", message);
                            queueMessageSender.sendMessage(queue, message);
                        }));
    }
}
