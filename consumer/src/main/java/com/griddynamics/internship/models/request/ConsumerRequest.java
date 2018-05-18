package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonRootName("consumer")
public class ConsumerRequest {

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${queue:#{null}}")
    private String queue;

    @Value("${topic:#{null}}")
    private String topic;
}
