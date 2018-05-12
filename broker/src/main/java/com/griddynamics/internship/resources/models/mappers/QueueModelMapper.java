package com.griddynamics.internship.resources.models.mappers;

import com.griddynamics.internship.models.db.Queue;
import com.griddynamics.internship.models.response.QueueResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QueueModelMapper {

    private ModelMapper modelMapper;

    public QueueModelMapper() {
        modelMapper = new ModelMapper();
    }

    public QueueResponse convertToResponseObject(Queue queue) {
        QueueResponse queueResponse = modelMapper.map(queue, QueueResponse.class);
        return queueResponse;
    }
}
