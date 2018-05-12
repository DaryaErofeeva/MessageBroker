package com.griddynamics.internship;

import com.griddynamics.internship.resources.QueueResource;
import com.griddynamics.internship.resources.TopicResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        registerClasses(QueueResource.class, TopicResource.class);
    }
}
