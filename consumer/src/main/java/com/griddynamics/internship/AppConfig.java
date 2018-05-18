package com.griddynamics.internship;

import com.griddynamics.internship.models.factories.ConsumerFactory;
import com.griddynamics.internship.models.response.ConsumerResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ConsumerFactory consumerFactory() {
        return new ConsumerFactory();
    }

    @Bean
    public ConsumerResponse consumerResponse() throws Exception {
        return consumerFactory().getObject();
    }
}
