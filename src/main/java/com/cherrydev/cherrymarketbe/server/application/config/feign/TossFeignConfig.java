package com.cherrydev.cherrymarketbe.server.application.config.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class TossFeignConfig {

    @Value("${toss.payment.clientkey}")
    private String clientSecret;
    public static final String TOSS_AUTHORIZATION_PREFIX = "Basic ";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate ->
                requestTemplate.header(AUTHORIZATION, TOSS_AUTHORIZATION_PREFIX + clientSecret);
    }

}
