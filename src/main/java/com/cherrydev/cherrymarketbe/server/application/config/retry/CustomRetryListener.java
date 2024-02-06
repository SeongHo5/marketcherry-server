package com.cherrydev.cherrymarketbe.server.application.config.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

@Slf4j(topic = "retryLogger")
public class CustomRetryListener implements RetryListener {
    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        log.info("Retryable 메서드 재시도 시작 - {}", context.getRetryCount());
        return true;
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.error("Retryable method failed with exception: {}", throwable.getMessage());
        log.info("Retry Count: {}", context.getRetryCount());
    }

}
