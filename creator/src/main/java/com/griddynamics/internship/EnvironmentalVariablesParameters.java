package com.griddynamics.internship;

public class EnvironmentalVariablesParameters extends ParametersSetter {

    @Override
    protected void setParameters() throws NumberFormatException {
        getParameters().setParameters(System.getenv("ROOT_FOLDER"),
                Integer.valueOf(System.getenv("FOLDERS_NUMBER")),
                Integer.valueOf(System.getenv("FILES_NUMBER")));
    }
}