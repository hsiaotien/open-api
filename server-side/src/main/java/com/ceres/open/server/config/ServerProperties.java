package com.ceres.open.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "websocket.server")
public class ServerProperties {
    private String hostName;
    private int port;
}
