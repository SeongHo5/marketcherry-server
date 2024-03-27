package com.cherrydev.cherrymarketbe.server.application.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 분산 락을 적용하기 위한 어노테이션
 * <p>
 *     - keyName: 락을 걸 key의 이름<br>
 *     - waitTime: 락을 얻기 위해 대기하는 시간<br>
 *     - leaseTime: 락을 유지하는 시간<br>
 *     - timeUnit: 시간 단위
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String keyName() default "";
    long waitTime() default 10L;
    long leaseTime() default 30L;
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
