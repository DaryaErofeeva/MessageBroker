package com.griddynamics.internship.resources.model.mappers;

import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.request.MessageRequest;
import com.griddynamics.internship.models.response.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageModelMapper {

    private ModelMapper modelMapper;

    public MessageModelMapper() {
        modelMapper = new ModelMapper();
    }

    public Message convertToEntity(MessageRequest messageRequest) {
        return modelMapper.map(messageRequest, Message.class);
    }

    public MessageResponse convertToResponseObject(Message message) {
        return modelMapper.map(message, MessageResponse.class);
    }
}
