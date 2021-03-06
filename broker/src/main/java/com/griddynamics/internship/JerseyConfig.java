package com.griddynamics.internship;

import com.griddynamics.internship.resources.ConsumerResource;
import com.griddynamics.internship.resources.QueueResource;
import com.griddynamics.internship.resources.TopicResources;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        registerClasses(QueueResource.class, TopicResources.class, ConsumerResource.class);
    }
}
