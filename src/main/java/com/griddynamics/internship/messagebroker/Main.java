package com.griddynamics.internship.messagebroker;

public class Main {
    private static final Parameters parameters = new Parameters();

    public static void main(String[] args) {
        if (setParameters(args))
            new Creator(parameters).create();
    }

    private static boolean setParameters(String[] args) {
        return args.length > 0 && MainParameters.setParameters(parameters, args) ||
                SystemPropertiesParameters.setParameters(parameters) ||
                EnvironmentalVariablesParameters.setParameters(parameters);
    }
}
