package com.cherrydev.cherrymarketbe.server.application.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Aspect
@Component
@Slf4j(topic = "exceptionLogger")
public class ExceptionLoggingAspect {

    @AfterThrowing(pointcut = "execution(* com..controller.*.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        handleExceptionInternal(joinPoint, ex);
    }

    private void handleExceptionInternal(JoinPoint joinPoint, Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        log.error("Resolved Exception: {} - Reason: {} / FROM {} / StackTrace: {}",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                joinPoint.getSignature().toShortString(),
                exceptionAsString
        );
    }

}
