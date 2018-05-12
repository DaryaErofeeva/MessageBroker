package com.griddynamics.internship.dao;

import com.griddynamics.internship.dao.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class DAOFactory {
    @Autowired
    private ConsumerDAO consumerDAO;

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private ProducerDAO producerDAO;

    @Autowired
    private QueueDAO queueDAO;

    @Autowired
    private QueueMessageDAO queueMessageDAO;

    @Autowired
    private TopicDAO topicDAO;

    @Autowired
    private TopicConsumerDAO topicConsumerDAO;

    @Autowired
    private TopicMessageDAO topicMessageDAO;

    public ConsumerDAO getConsumerDAO() {
        return consumerDAO;
    }

    public MessageDAO getMessageDAO() {
        return messageDAO;
    }

    public ProducerDAO getProducerDAO() {
        return producerDAO;
    }

    public QueueDAO getQueueDAO() {
        return queueDAO;
    }

    public QueueMessageDAO getQueueMessageDAO() {
        return queueMessageDAO;
    }

    public TopicDAO getTopicDAO() {
        return topicDAO;
    }

    public TopicConsumerDAO getTopicConsumerDAO() {
        return topicConsumerDAO;
    }

    public TopicMessageDAO getTopicMessageDAO() {
        return topicMessageDAO;
    }
}
