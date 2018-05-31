package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@JsonRootName("topic")
@Data
@NoArgsConstructor
public class TopicResponse {
    private int id;
    private String name;
    private List<ConsumerResponse> consumers;
    private List<MessageResponse> messages;
}
