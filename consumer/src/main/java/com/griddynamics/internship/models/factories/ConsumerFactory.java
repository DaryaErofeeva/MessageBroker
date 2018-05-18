package com.griddynamics.internship.models.factories;

import com.griddynamics.internship.models.request.ConsumerRequest;
import com.griddynamics.internship.models.response.ConsumerResponse;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConsumerFactory implements FactoryBean<ConsumerResponse> {

    @Autowired
    private ConsumerRequest consumerRequest;

    @Override
    public ConsumerResponse getObject() throws Exception {
        return new RestTemplate()
                .postForObject("http://localhost:8080/broker/v1/consumer",
                        new HttpEntity<>(consumerRequest), ConsumerResponse.class);
    }

    @Override
    public Class<?> getObjectType() {
        return ConsumerResponse.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
