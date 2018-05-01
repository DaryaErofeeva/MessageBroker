package com.griddynamics.internship;

import com.beust.jcommander.JCommander;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

public class Main {

    public static void main(String[] args) {
        CreatorJCommander jCommander = new CreatorJCommander();

        Parameters parameters = Parameters.getFilledParameters(jCommander, args);

        if (parameters.isValid() && !parameters.isHelp())
            new Creator(parameters).create();
        else jCommander.usage();
    }
}
