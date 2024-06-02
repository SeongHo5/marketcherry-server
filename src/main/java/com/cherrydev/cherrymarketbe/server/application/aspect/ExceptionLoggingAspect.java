package com.cherrydev.cherrymarketbe.server.application.aspect;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
@Slf4j(topic = "exceptionLogger")
public class ExceptionLoggingAspect {

    @AfterThrowing(pointcut = "execution(* com..controller.*.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        handleExceptionInternal(joinPoint, ex);
    }

    private void handleExceptionInternal(JoinPoint joinPoint, Throwable ex) {
        log.error("Resolved Exception: {} - Reason: {} / FROM {}",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                joinPoint.getSignature().toShortString()
        );
    }

}
