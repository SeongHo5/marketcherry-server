package com.cherrydev.cherrymarketbe.server.application.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j(topic = "executionTimeLogger")
@Component
public class ExecutionTimeLoggingAspect {

    @Around("execution(* com.cherrydev.cherrymarketbe..service..*.*(..)) " +
            "|| execution(* com.cherrydev.cherrymarketbe..controller..*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("{}.{} executed in {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    executionTime
            );
        }
    }
}
