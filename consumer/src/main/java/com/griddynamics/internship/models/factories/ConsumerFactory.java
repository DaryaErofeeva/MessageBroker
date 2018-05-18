package com.griddynamics.internship.models.factories;

import com.griddynamics.internship.LogService;
import com.griddynamics.internship.models.request.ConsumerRequest;
import com.griddynamics.internship.models.response.ConsumerResponse;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class ConsumerFactory implements FactoryBean<ConsumerResponse> {

    @Autowired
    private ConsumerRequest consumerRequest;

    @Autowired
    private LogService logService;

    @Override
    public ConsumerResponse getObject() throws Exception {
        try {
            return new RestTemplate()
                    .postForObject("http://localhost:8080/broker/v1/consumer",
                            new HttpEntity<>(consumerRequest), ConsumerResponse.class);
        } catch (HttpClientErrorException ex) {
            logService.log(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            logService.log(new ResponseMessage("Broker isn't running").toString());
        }
        return new ConsumerResponse();
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
