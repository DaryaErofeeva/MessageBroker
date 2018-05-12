package com.griddynamics.internship.dao;

import com.griddynamics.internship.dao.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DAOFactory {

    @Autowired
    private ConsumerDAO consumerDAO;

    @Autowired
    private QueueConsumerMessageDAO queueConsumerMessageDAO;

    @Autowired
    private QueueDAO queueDAO;

    @Autowired
    private TopicConsumerMessageDAO topicConsumerMessageDAO;

    @Autowired
    private TopicDAO topicDAO;

    public ConsumerDAO getConsumerDAO() {
        return consumerDAO;
    }

    public QueueConsumerMessageDAO getQueueConsumerMessageDAO() {
        return queueConsumerMessageDAO;
    }

    public QueueDAO getQueueDAO() {
        return queueDAO;
    }

    public TopicConsumerMessageDAO getTopicConsumerMessageDAO() {
        return topicConsumerMessageDAO;
    }

    public TopicDAO getTopicDAO() {
        return topicDAO;
    }
}
