package com.griddynamics.internship;

import com.griddynamics.internship.resources.ConsumerResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        registerClasses(ConsumerResource.class);
    }
}
