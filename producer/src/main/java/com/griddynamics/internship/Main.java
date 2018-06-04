package com.griddynamics.internship;

import com.griddynamics.internship.parameters.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

@SpringBootApplication
@DependsOn("parametersFactory")
public class Main implements CommandLineRunner {
    @Autowired
    private Parameters parameters;

    @Autowired
    private ProducerJCommander jCommander;

    @Autowired
    private ProducerService producer;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            System.setProperty("server.host", InetAddress.getLocalHost().getHostAddress());
            System.setProperty("server.port", String.valueOf(serverSocket.getLocalPort()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(System.getProperty("server.host")+":"+System.getProperty("server.port"));

        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }



    @Override
    public void run(String... args) {
        if (parameters.isValid() && !parameters.isHelp())
            producer.produce();
        else jCommander.usage();
    }
}
