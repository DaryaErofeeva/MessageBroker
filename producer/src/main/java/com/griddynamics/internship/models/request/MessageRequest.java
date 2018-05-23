package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonRootName("message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String content;
}
