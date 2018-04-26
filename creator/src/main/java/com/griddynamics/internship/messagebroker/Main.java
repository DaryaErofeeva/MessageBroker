package com.griddynamics.internship.messagebroker;

public class Main {

    private static final Parameters parameters = new Parameters();

    public static void main(String[] args) {
        if (parameters.setParameters(args))
            new Creator(parameters).create();
    }
}
