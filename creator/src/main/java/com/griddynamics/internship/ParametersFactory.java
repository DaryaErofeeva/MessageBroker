package com.griddynamics.internship;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "parametersFactory")
public class ParametersFactory implements FactoryBean<Parameters> {
    @Autowired
    private MainParameters mainParameters;

    @Autowired
    private SystemPropertiesParameters systemPropertiesParameters;

    @Autowired
    private EnvironmentalVariablesParameters environmentalVariablesParameters;

    @Override
    public Parameters getObject() throws Exception {
        mainParameters
                .setNextParametersSetter(
                        systemPropertiesParameters
                                .setNextParametersSetter(
                                        environmentalVariablesParameters));

        return mainParameters.getFilledParameters();
    }

    @Override
    public Class<Parameters> getObjectType() {
        return Parameters.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
