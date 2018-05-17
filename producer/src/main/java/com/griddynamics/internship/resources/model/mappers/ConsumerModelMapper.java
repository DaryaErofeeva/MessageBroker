package com.griddynamics.internship.resources.model.mappers;

import com.griddynamics.internship.models.entities.Consumer;
import com.griddynamics.internship.models.request.ConsumerRequest;
import com.griddynamics.internship.models.response.ConsumerResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConsumerModelMapper {
    private ModelMapper modelMapper;

    public ConsumerModelMapper() {
        modelMapper = new ModelMapper();
    }

    public Consumer convertToEntity(ConsumerRequest consumerRequest) {
        return modelMapper.map(consumerRequest, Consumer.class);
    }

    public ConsumerResponse convertToResponseObject(Consumer consumer) {
        return modelMapper.map(consumer, ConsumerResponse.class);
    }
}
