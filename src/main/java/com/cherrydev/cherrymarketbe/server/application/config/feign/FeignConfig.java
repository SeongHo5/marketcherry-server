package com.cherrydev.cherrymarketbe.server.application.config.feign;

import feign.Logger;
import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableFeignClients(basePackages = "com.cherrydev.cherrymarketbe.server")
public class FeignConfig {

    /**
     * Feign Client 로깅 레벨 설정
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Feign Client Timeout 설정
     * <p>
     * connectTimeout: 연결 시도 시간 - 기본값 5초
     * <p>
     * readTimeout: 읽기 시간 초과 시간 - 기본값 10초
     * <p>
     * followRedirects: 리다이렉트를 자동으로 따를지 여부 - 기본값 true
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(Duration.ofSeconds(5), Duration.ofSeconds(10), true);
    }

}
