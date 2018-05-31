package com.griddynamics.internship;

import com.griddynamics.internship.models.request.MessageRequest;
import com.griddynamics.internship.models.response.ResponseMessage;
import com.griddynamics.internship.parameters.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProducerService {

    @Autowired
    private Parameters parameters;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private LogService logService;

    public void produce() {
        try {
            for (int i = 0; i < parameters.getMessages(); i++) {
                sendMessage("queue", parameters.getQueue());
                sendMessage("topic", parameters.getTopic());
            }
        } catch (ResourceAccessException ex) {
            logService.log(new ResponseMessage("Broker isn't running").toString());
        }

    }

    private void sendMessage(String sourceType, String sourceName) {
        if (sourceName != null && !sourceName.isEmpty())
            restTemplateBuilder.build()
                    .put("http://" + parameters.getBrokerHost() + ":" + parameters.getBrokerPort() + "/broker/v1/producer/" + sourceType + "/" + sourceName,
                            new MessageRequest(getContent()));
    }

    private String getContent() {
        return String.format("%010d", ThreadLocalRandom.current().nextLong(0, 10000000000L));
    }
}
