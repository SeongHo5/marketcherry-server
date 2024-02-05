package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class InsufficientStockException extends ApplicationException {

    private String goodsCode;

    public InsufficientStockException(ExceptionStatus status) {
        super(status);
    }

    public InsufficientStockException(ExceptionStatus status, @Nullable String goodsCode) {
        super(status);
        this.goodsCode = goodsCode;
    }
}
