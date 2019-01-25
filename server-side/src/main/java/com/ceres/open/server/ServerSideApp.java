package com.ceres.open.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServerSideApp {

    public static void main(String[] args) {
        SpringApplication.run(ServerSideApp.class, args);
    }
}
