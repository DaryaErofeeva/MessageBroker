package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonRootName("consumer")
@Data
@NoArgsConstructor
public class ConsumerRequest {
    private String host;
    private String port;
    private String queue;
    private String topic;
}
