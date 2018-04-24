package com.dyerofieieva;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class MainParameters {

    public static boolean setParameters(Parameters parameters, String[] args) {
        return handleInputArgs(parameters, args);
    }

    private static boolean handleInputArgs(Parameters parameters, String[] args) {
        JCommander jCommander = JCommander.newBuilder()
                .addObject(parameters)
                .build();
        jCommander.setProgramName("messageBroker");

        try {
            jCommander.parse(args);

            if (parameters.isHelp()) {
                showUsage(jCommander);
                return false;
            }
        } catch (ParameterException ex) {
            ex.printStackTrace();
            showUsage(jCommander);
            return false;
        }

        return true;
    }

    private static void showUsage(JCommander jCommander) {
        jCommander.usage();
    }
}
