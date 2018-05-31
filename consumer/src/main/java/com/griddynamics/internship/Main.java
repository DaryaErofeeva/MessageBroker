package com.griddynamics.internship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            System.setProperty("server.host", InetAddress.getLoopbackAddress().getHostName());
            System.setProperty("server.port", String.valueOf(serverSocket.getLocalPort()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        SpringApplication.run(Main.class, args);
    }
}
