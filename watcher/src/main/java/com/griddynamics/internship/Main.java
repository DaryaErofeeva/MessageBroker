package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private WatcherService watcherService;

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        watcherService.poll();
    }
}
