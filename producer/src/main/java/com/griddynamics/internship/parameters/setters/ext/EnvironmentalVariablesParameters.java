package com.griddynamics.internship.parameters.setters.ext;

import com.griddynamics.internship.parameters.setters.ParametersSetter;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentalVariablesParameters extends ParametersSetter {

    @Override
    protected void setParameters() throws NumberFormatException {
        getParameters().setParameters(System.getenv("QUEUE"),
                System.getenv("TOPIC"),
                Integer.valueOf(System.getenv("MESSAGES")),
                System.getenv("BROKER_HOST"),
                System.getenv("BROKER_PORT"));
    }
}
