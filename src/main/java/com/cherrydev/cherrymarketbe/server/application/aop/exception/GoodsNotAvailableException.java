package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class GoodsNotAvailableException extends ApplicationException {


    public GoodsNotAvailableException(ExceptionStatus status) {
        super(status);
    }
}
