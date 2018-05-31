package com.griddynamics.internship;

import com.griddynamics.internship.models.request.ConsumerRequest;
import com.griddynamics.internship.models.response.ConsumerResponse;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Component
public class ConsumerFactory implements FactoryBean<ConsumerResponse> {

    @Value("${broker.host:localhost}")
    private String brokerHost;

    @Value("${broker.port:8080}")
    private String brokerPort;

    @Autowired
    private ConsumerRequest consumerRequest;

    @Autowired
    private LogService logService;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public ConsumerResponse getObject() throws Exception {
        try {
            return restTemplateBuilder.build()
                    .postForObject("http://" + brokerHost + ":" + brokerPort + "/broker/v1/consumer",
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
