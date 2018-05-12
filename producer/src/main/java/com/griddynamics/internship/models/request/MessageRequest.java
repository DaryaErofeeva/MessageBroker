package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonRootName("message")
@Data
@NoArgsConstructor
public class MessageRequest {
    private String content;
    private String state;
}
