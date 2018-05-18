package com.griddynamics.internship.models.response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConsumerResponse {
    private int id;
    private String host;
    private String port;
}
