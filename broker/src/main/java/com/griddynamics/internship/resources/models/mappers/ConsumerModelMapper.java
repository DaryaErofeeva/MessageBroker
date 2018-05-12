package com.griddynamics.internship.resources.models.mappers;

import com.griddynamics.internship.models.db.Consumer;
import com.griddynamics.internship.models.response.ConsumerResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConsumerModelMapper {
    private ModelMapper modelMapper;

    public ConsumerModelMapper() {
        modelMapper = new ModelMapper();
    }

    public ConsumerResponse convertToResponseObject(Consumer consumer) {
        ConsumerResponse consumerResponse = modelMapper.map(consumer, ConsumerResponse.class);
        consumerResponse.setId(consumer.getId());
        consumerResponse.setName(consumer.getName());
        return consumerResponse;
    }
}
