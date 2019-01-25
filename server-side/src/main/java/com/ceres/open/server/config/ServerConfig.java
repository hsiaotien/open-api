package com.ceres.open.server.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class ServerConfig {

    @Bean
    public InetSocketAddress inetSocketAddress(ServerProperties props){
        return new InetSocketAddress(props.getHostName(), props.getPort());
    }
}
