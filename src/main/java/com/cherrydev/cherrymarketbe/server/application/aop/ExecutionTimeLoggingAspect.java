package com.cherrydev.cherrymarketbe.server.application.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Slf4j(topic = "executionTimeLogger")
@Component
public class ExecutionTimeLoggingAspect {

    @Around("execution(* com..controller.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String methodSignature = joinPoint.getSignature().getName();
        try {
            stopWatch.start();
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Exception occurred while measuring execution time of method: {}", methodSignature);
            throw throwable;
        } finally {
            stopWatch.stop();
            log.info("{} 실행 시간 : {} ms", methodSignature, stopWatch.getTotalTimeMillis());
        }
    }
}
