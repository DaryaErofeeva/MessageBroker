package com.dyerofieieva;

public class EnvironmentalVariablesParameters {
    public static boolean setParameters(Parameters parameters) {
        try {
            parameters.setReceivedParameters(System.getenv("ROOT_FOLDER"),
                    Integer.valueOf(System.getenv("FOLDERS_NUMBER")),
                    Integer.valueOf(System.getenv("FILES_NUMBER")));
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
