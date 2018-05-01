package com.griddynamics.internship;

import com.beust.jcommander.ParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class MainParameters extends ParametersSetter {

    @Autowired
    private CreatorJCommander jCommander;

    @Autowired
    private ApplicationArguments args;

    @Override
    protected void setParameters() throws ParameterException {
        jCommander.addObject(getParameters());
        jCommander.parse(args.getSourceArgs());
    }
}
