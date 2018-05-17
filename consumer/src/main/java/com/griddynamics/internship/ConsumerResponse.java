package com.griddynamics.internship;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConsumerResponse {
    private int id;
    private String port;
}
