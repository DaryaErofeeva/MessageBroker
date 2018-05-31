package com.griddynamics.internship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(Main.class, args);
    }
}
