package com.griddynamics.internship;

import com.beust.jcommander.ParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class ParametersSetter {
    @Autowired
    private Parameters parameters;
    private ParametersSetter nextParametersSetter;

//    public ParametersSetter() {
//        parameters = new Parameters();
//    }

    public Parameters getParameters() {
        return parameters;
    }

    public ParametersSetter setNextParametersSetter(ParametersSetter nextParametersSetter) {
        this.nextParametersSetter = nextParametersSetter;
        return this;
    }

    public Parameters getFilledParameters() {
        try {
            setParameters();
        } catch (ParameterException | NumberFormatException ex) {
            if (nextParametersSetter != null)
                parameters = nextParametersSetter.getFilledParameters();
        }

        return parameters;
    }

    protected abstract void setParameters();
}
