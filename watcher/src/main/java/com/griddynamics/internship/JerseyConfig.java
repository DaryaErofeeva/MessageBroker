package com.griddynamics.internship;

import com.griddynamics.internship.rest.resources.ChangeResource;
import com.griddynamics.internship.rest.resources.ChannelResource;
import com.griddynamics.internship.rest.resources.MessageResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ChannelResource.class);
        register(MessageResource.class);
        register(ChangeResource.class);
    }
}
