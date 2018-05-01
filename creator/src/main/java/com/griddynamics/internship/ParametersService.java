package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ParametersService {

    @Autowired
    private Parameters parameters;

    @Autowired
    private CreatorJCommander jCommander;

    @Autowired
    private MainParameters mainParameters;

    @Autowired
    private SystemPropertiesParameters systemPropertiesParameters;

    @Autowired
    private EnvironmentalVariablesParameters environmentalVariablesParameters;

    @Autowired
    private CreatorService creator;

    @Bean
    public Parameters getFilledParameters() {
        mainParameters
                .setNextParametersSetter(
                        systemPropertiesParameters
                                .setNextParametersSetter(
                                        environmentalVariablesParameters));

        return mainParameters.getFilledParameters();
    }

    public void run() {
        if (parameters.isValid() && !parameters.isHelp())
            creator.create();
        else jCommander.usage();
    }
}
