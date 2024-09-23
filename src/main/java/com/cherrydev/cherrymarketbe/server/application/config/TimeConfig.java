package com.cherrydev.cherrymarketbe.server.application.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
