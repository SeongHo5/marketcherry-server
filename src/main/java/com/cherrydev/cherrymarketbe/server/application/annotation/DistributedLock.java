package com.cherrydev.cherrymarketbe.server.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String keyName() default "KEY";
    long waitTime() default 10L;
    long leaseTime() default 30L;
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
