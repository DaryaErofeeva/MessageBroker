package com.griddynamics.internship;

import com.griddynamics.internship.models.factories.ConsumerFactory;
import com.griddynamics.internship.models.response.ConsumerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private ConsumerFactory consumerFactory;

    @Bean
    public ConsumerResponse consumerResponse() throws Exception {
        return consumerFactory.getObject();
    }
}
