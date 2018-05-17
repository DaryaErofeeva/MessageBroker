package com.griddynamics.internship;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonRootName("response")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    private String message;
}
