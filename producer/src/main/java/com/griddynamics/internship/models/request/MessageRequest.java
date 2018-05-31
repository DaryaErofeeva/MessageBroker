package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonRootName("message")
@Data
@AllArgsConstructor
public class MessageRequest {
    private String content;
}
