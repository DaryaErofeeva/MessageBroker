package com.griddynamics.internship.resources.models.mappers;

import com.griddynamics.internship.models.db.Topic;
import com.griddynamics.internship.models.response.TopicResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TopicModelMapper {

    private ModelMapper modelMapper;

    public TopicModelMapper() {
        modelMapper = new ModelMapper();
    }

    public TopicResponse convertToResponseObject(Topic topic) {
        TopicResponse topicResponse = modelMapper.map(topic, TopicResponse.class);
        return topicResponse;
    }
}
