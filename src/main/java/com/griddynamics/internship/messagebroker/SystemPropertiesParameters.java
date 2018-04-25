package com.griddynamics.internship.messagebroker;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SystemPropertiesParameters {

    public static boolean setParameters(Parameters parameters) {
        loadMissedSystemPropertiesFromFile();
        try {
            parameters.setReceivedParameters(System.getProperty("rootFolder"),
                    Integer.valueOf(System.getProperty("foldersNumber")),
                    Integer.valueOf(System.getProperty("filesNumber")));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static void loadMissedSystemPropertiesFromFile() {
        Properties fileProperties = new Properties();

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("my.properties")) {
            fileProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSystemProperties(fileProperties, "rootFolder", "foldersNumber", "filesNumber");
    }

    private static void setSystemProperties(Properties fileProperties, String... propertyNames) {
        for (String propertyName : propertyNames) {
            if (fileContainsMissed(fileProperties, propertyName))
                System.setProperty(propertyName, fileProperties.getProperty(propertyName));
        }
    }

    private static boolean fileContainsMissed(Properties fileProperties, String propertyName){
        return !isSet(System.getProperties(), propertyName) &&
                isSet(fileProperties, propertyName);
    }

    private static boolean isSet(Properties properties, String propertyName) {
        return !(properties.getProperty(propertyName) == null ||
                properties.getProperty(propertyName).equals(""));
    }
}
