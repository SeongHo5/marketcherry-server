package com.cherrydev.cherrymarketbe.server.application.aop;

import com.cherrydev.cherrymarketbe.server.application.annotation.DistributedLock;
import com.cherrydev.cherrymarketbe.server.application.common.utils.CustomSpelParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j(topic = "distributedLockAspect")
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String REDISSON_KEY_PREFIX = "LOCK::";

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object aroundLockPointcut(
            ProceedingJoinPoint joinPoint,
            DistributedLock distributedLock
    ) throws Throwable {
        String key = getKeyFromMethodSignature(joinPoint, distributedLock);
        RLock lock = redissonClient.getLock(key);
        try {
            log.info("Acquiring Lock: {}", key);
            if (!tryAcquiringLock(lock, distributedLock)) {
                return false;
            }
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            releaseLock(lock);
        }
    }

    private String getKeyFromMethodSignature(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        String key = CustomSpelParser.getDynamicValue(
                ((MethodSignature) joinPoint.getSignature()).getParameterNames(),
                joinPoint.getArgs(),
                distributedLock.keyName()
        ).toString();
        return REDISSON_KEY_PREFIX + key;
    }

    private boolean tryAcquiringLock(RLock lock, DistributedLock distributedLock) throws InterruptedException {
        return lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
    }

    private void releaseLock(RLock lock) {
        try {
            lock.unlock();
        } catch (IllegalMonitorStateException e) {
            log.error("Error While Releasing Lock", e);
        }
    }
}

