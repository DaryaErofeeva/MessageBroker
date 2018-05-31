package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonRootName("consumer")
@Data
@NoArgsConstructor
public class ConsumerResponse {
    private int id;
    private String host;
    private String port;
}
