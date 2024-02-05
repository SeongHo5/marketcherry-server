package com.cherrydev.cherrymarketbe.server.application.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j(topic = "requestLogger")
public class RequestLoggingAspect {

    @Before("execution(* com..*.controller.*.*(..))")
    public void logBeforeRequest() {
        HttpServletRequest request = getCurrentHttpRequest();
        log.info("Incoming Request: {} {} From {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
        );
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }
}
