package com.griddynamics.internship.messagebroker;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> properties = getSystemPropertiesMap();

        //check if any property is missed
        if (properties.values().contains(null) || properties.values().contains("")) {

            //load properties from file
            try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("my.properties")) {
                System.getProperties().load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setSystemPropertiesIfSet(properties);
    }

    private static Map<String, String> getSystemPropertiesMap() {
        Map<String, String> properties = new HashMap<>();
        putSystemProperty(properties, "rootFolder", "foldersNumber", "filesNumber");
        return properties;
    }

    private static void putSystemProperty(Map<String, String> properties, String... propertiesNames) {
        for (String propertyName : propertiesNames)
            properties.put(propertyName, System.getProperty(propertyName));
    }

    private static void setSystemPropertiesIfSet(Map<String, String> properties) {
        for (String key : properties.keySet())
            if (properties.get(key) != null && !properties.get(key).equals(""))
                System.setProperty(key, properties.get(key));
    }
}
