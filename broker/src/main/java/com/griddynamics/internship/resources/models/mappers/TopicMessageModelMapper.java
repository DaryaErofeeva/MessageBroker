package com.griddynamics.internship.resources.models.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.TopicConsumer;
import com.griddynamics.internship.models.db.TopicMessage;
import com.griddynamics.internship.models.request.TopicMessageRequest;
import com.griddynamics.internship.models.response.TopicMessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicMessageModelMapper {
    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private ConsumerModelMapper consumerModelMapper;

    private ModelMapper modelMapper;

    public TopicMessageModelMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    public TopicMessage convertToEntity(TopicMessageRequest topicMessageRequest, String topicName) {
        TopicMessage topicMessage = modelMapper.map(topicMessageRequest, TopicMessage.class);
        topicMessage.setTopicConsumer(getTopicConsumer(topicName, topicMessageRequest.getConsumerId()));
        topicMessage.setMessage(daoFactory.getMessageDAO().getEntityById(topicMessageRequest.getMessageId()));
        return topicMessage;
    }

    private TopicConsumer getTopicConsumer(String topicName, int consumerId) {
        TopicConsumer topicConsumer = new TopicConsumer(
                daoFactory.getTopicDAO().getEntityByName(topicName),
                daoFactory.getConsumerDAO().getEntityById(consumerId));
        topicConsumer.setId(daoFactory.getTopicConsumerDAO().getIdByEntity(topicConsumer));
        return topicConsumer;
    }

    public TopicMessageResponse convertToResponseObject(TopicMessage topicMessage) {
        TopicMessageResponse topicMessageResponse = modelMapper.map(topicMessage, TopicMessageResponse.class);
        topicMessageResponse.setConsumer(consumerModelMapper.convertToResponseObject(topicMessage.getTopicConsumer().getConsumer()));
        return topicMessageResponse;
    }
}
