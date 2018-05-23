package com.griddynamics.internship.models.factories;

import com.griddynamics.internship.AppConfig;
import com.griddynamics.internship.LogService;
import com.griddynamics.internship.models.request.ConsumerRequest;
import com.griddynamics.internship.models.response.ConsumerResponse;
import com.griddynamics.internship.models.response.ResponseMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConsumerFactoryTest {

    @MockBean
    private ConsumerRequest consumerRequest;

    @MockBean
    private LogService logService;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private AppConfig appConfig;

    @Autowired
    private ConsumerFactory consumerFactory;

    @Before
    public void beforeGetObject() throws Exception {
        MockitoAnnotations.initMocks(this);
        given(restTemplateBuilder.build()).willReturn(restTemplate);
    }

    @Test
    public void getObject() throws Exception {
        consumerFactory.getObject();
        verify(restTemplate, times(1))
                .postForObject("http://localhost:8080/broker/v1/consumer",
                        new HttpEntity<>(consumerRequest), ConsumerResponse.class);
    }

    @Test
    public void getObject_HttpClientErrorException() throws Exception {
        when(restTemplate.
                postForObject("http://localhost:8080/broker/v1/consumer",
                        new HttpEntity<>(consumerRequest), ConsumerResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        consumerFactory.getObject();
        verify(logService, times(1))
                .log(new HttpClientErrorException(HttpStatus.BAD_REQUEST).getResponseBodyAsString());
    }

    @Test
    public void getObject_ResourceAccessException() throws Exception {
        when(restTemplate.
                postForObject("http://localhost:8080/broker/v1/consumer",
                        new HttpEntity<>(consumerRequest), ConsumerResponse.class))
                .thenThrow(new ResourceAccessException(""));

        consumerFactory.getObject();
        verify(logService, times(1))
                .log(new ResponseMessage("Broker isn't running").toString());
    }

}
