package com.griddynamics.internship.models.request;


import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@JsonRootName("message")
@Data
public class MessageRequest {
    private String content;
}