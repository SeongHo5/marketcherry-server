package com.cherrydev.cherrymarketbe.server.application.aspect;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j(topic = "executionTimeLogger")
@Component
public class ExecutionTimeLoggingAspect {

    @Around("execution(* com.cherrydev.cherrymarketbe..controller..*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            if (executionTime > 500) {
                log.info("{}.{} executed in {} ms",
                        joinPoint.getSignature().getDeclaringType().getSimpleName(),
                        joinPoint.getSignature().getName(),
                        executionTime
                );
            }
        }
    }
}
