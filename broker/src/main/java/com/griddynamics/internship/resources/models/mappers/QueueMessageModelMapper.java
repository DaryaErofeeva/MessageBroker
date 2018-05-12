package com.griddynamics.internship.resources.models.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.QueueMessage;
import com.griddynamics.internship.models.request.QueueMessageRequest;
import com.griddynamics.internship.models.response.QueueMessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueMessageModelMapper {
    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private ConsumerModelMapper consumerModelMapper;

    private ModelMapper modelMapper;

    public QueueMessageModelMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    public QueueMessage convertToEntity(QueueMessageRequest queueMessageRequest, String queueName) {
        QueueMessage queueMessage = modelMapper.map(queueMessageRequest, QueueMessage.class);
        queueMessage.setQueue(daoFactory.getQueueDAO().getEntityByName(queueName));
        queueMessage.setMessage(daoFactory.getMessageDAO().getEntityById(queueMessageRequest.getMessageId()));
        return queueMessage;
    }

    public QueueMessageResponse convertToResponseObject(QueueMessage queueMessage) {
        QueueMessageResponse queueMessageResponse = modelMapper.map(queueMessage, QueueMessageResponse.class);
        queueMessageResponse.setConsumer(consumerModelMapper.convertToResponseObject(queueMessage.getQueue().getConsumer()));
        return queueMessageResponse;
    }
}
