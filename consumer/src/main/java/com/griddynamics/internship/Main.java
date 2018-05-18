package com.griddynamics.internship;

import com.griddynamics.internship.models.response.ConsumerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ConsumerResponse consumerResponse;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            System.setProperty("server.port", String.valueOf(serverSocket.getLocalPort()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(consumerResponse);
    }
}
