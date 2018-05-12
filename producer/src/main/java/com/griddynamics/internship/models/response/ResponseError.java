package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonRootName("error")
@Data
@AllArgsConstructor
public class ResponseError {
    private String message;
}
