package com.griddynamics.internship.resources.model.mappers;

import com.griddynamics.internship.models.Topic;
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
        return modelMapper.map(topic, TopicResponse.class);
    }
}
