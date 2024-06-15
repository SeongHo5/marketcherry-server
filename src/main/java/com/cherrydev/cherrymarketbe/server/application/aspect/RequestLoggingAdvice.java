package com.cherrydev.cherrymarketbe.server.application.aspect;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
@Component
@Slf4j(topic = "requestLogger")
public class RequestLoggingAdvice {

    @Before("execution(* com..*.controller.*.*(..))")
    public void logBeforeRequest() {
        if (log.isDebugEnabled()) {
            HttpServletRequest request = getCurrentHttpRequest();
            log.debug("Incoming Request: {} {}", request.getMethod(), request.getRequestURI());
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }
}
