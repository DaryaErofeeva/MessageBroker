package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;

@SpringBootApplication
@DependsOn("parametersFactory")
public class Main implements CommandLineRunner {
    @Autowired
    private Parameters parameters;

    @Autowired
    private CreatorJCommander jCommander;

    @Autowired
    private CreatorService creator;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (parameters.isValid() && !parameters.isHelp())
            creator.create();
        else jCommander.usage();
    }
}
