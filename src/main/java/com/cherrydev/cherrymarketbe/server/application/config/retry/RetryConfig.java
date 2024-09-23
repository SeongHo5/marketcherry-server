package com.cherrydev.cherrymarketbe.server.application.config.retry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class RetryConfig {

  @Bean
  public RetryListener customRetryListener() {
    return new CustomRetryListener();
  }
}
