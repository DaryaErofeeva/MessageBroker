package com.griddynamics.internship;

import com.beust.jcommander.ParameterException;

public class MainParameters extends ParametersSetter {

    private CreatorJCommander jCommander;
    private String[] args;

    public MainParameters(CreatorJCommander jCommander, String[] args) {
        this.jCommander = jCommander;
        this.args = args;
    }

    @Override
    protected void setParameters() throws ParameterException {
        jCommander.addObject(getParameters());
        jCommander.parse(args);
    }
}
