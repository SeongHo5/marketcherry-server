package com.cherrydev.cherrymarketbe.server.application.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private String host;
    private Integer port;
    private String password;

}
