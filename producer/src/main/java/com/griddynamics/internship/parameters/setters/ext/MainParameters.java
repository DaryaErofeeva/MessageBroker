package com.griddynamics.internship.parameters.setters.ext;

import com.beust.jcommander.ParameterException;
import com.griddynamics.internship.ProducerJCommander;
import com.griddynamics.internship.parameters.setters.ParametersSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class MainParameters extends ParametersSetter {

    @Autowired
    private ProducerJCommander jCommander;

    @Autowired
    private ApplicationArguments args;

    @Override
    protected void setParameters() throws ParameterException {
        jCommander.addObject(getParameters());
        jCommander.parse(args.getSourceArgs());
    }
}
