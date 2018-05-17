package com.griddynamics.internship;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerFactoryAppConfig {

    @Bean
    public ConsumerFactory consumerFactory() {
        return new ConsumerFactory();
    }

    @Bean
    public ConsumerResponse consumerResponse() throws Exception {
        return consumerFactory().getObject();
    }
}
