package com.griddynamics.internship.messagebroker;

public class EnvironmentalVariablesParameters {
    public static boolean setParameters(Parameters parameters) {
        try {
            parameters.setParameters(System.getenv("ROOT_FOLDER"),
                    Integer.valueOf(System.getenv("FOLDERS_NUMBER")),
                    Integer.valueOf(System.getenv("FILES_NUMBER")));
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
