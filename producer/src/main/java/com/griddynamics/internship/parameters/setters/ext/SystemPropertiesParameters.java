package com.griddynamics.internship.parameters.setters.ext;

import com.griddynamics.internship.Main;
import com.griddynamics.internship.parameters.setters.ParametersSetter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class SystemPropertiesParameters extends ParametersSetter {

    @Override
    protected void setParameters() throws NumberFormatException {
        loadMissedSystemPropertiesFromFile();

        getParameters().setParameters(System.getProperty("queue"),
                System.getProperty("topic"),
                Integer.valueOf(System.getProperty("messages")),
                System.getProperty("broker.host"),
                System.getProperty("broker.port"));
    }


    private void loadMissedSystemPropertiesFromFile() {
        Properties fileProperties = new Properties();

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("my.properties")) {
            fileProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSystemProperties(fileProperties, "queue", "topic", "messages", "broker.host", "broker.port");
    }

    private void setSystemProperties(Properties fileProperties, String... propertyNames) {
        for (String propertyName : propertyNames) {
            if (fileContainsMissed(fileProperties, propertyName))
                System.setProperty(propertyName, fileProperties.getProperty(propertyName));
        }
    }

    private boolean fileContainsMissed(Properties fileProperties, String propertyName) {
        return !isSet(System.getProperties(), propertyName) &&
                isSet(fileProperties, propertyName);
    }

    private boolean isSet(Properties properties, String propertyName) {
        return !(properties.getProperty(propertyName) == null ||
                properties.getProperty(propertyName).equals(""));
    }
}
