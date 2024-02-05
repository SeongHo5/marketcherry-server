package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class InsufficientStockException extends ApplicationException {

    private String goodsName;

    public InsufficientStockException(ExceptionStatus status) {
        super(status);
    }

    public InsufficientStockException(ExceptionStatus status, @Nullable String goodsName) {
        super(status);
        this.goodsName = goodsName;
    }
}
