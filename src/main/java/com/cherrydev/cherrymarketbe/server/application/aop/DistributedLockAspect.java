package com.cherrydev.cherrymarketbe.server.application.aop;

import com.cherrydev.cherrymarketbe.server.application.annotation.DistributedLock;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.CouldNotObtainLockException;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "DistributedLockAspect")
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(distributedLock)")
    public void lockPointcut(DistributedLock distributedLock) {
    }

    @Around(value = "lockPointcut(distributedLock) && args(goods, requestedQuantity)",
            argNames = "joinPoint,distributedLock,goods,requestedQuantity")
    public Object aroundLockPointcut(
            ProceedingJoinPoint joinPoint,
            DistributedLock distributedLock,
            Goods goods,
            int requestedQuantity
    ) throws Throwable {
        RLock lock = redissonClient.getLock(goods.getCode());
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS);
            if (isLocked) {
                log.info("Lock acquired for key: {}", goods.getCode());
                return joinPoint.proceed();
            } else {
                throw new CouldNotObtainLockException("Could not obtain lock");
            }
        } finally {
            if (isLocked) {
                log.info("Releasing lock for key: {}", goods.getCode());
                lock.unlock();
            }
        }
    }
}
