package com.ceres.open.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class ClientConfig {

    @Bean
    public URI uri() {
        URI uri = null;
        try {
            uri = new URI("ws://localhost:8887");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }
}
