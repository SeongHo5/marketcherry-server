package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final Integer statusCode;
    private final String message;

    protected ApplicationException(ExceptionStatus ex) {
        this.statusCode = ex.getStatusCode();
        this.message = ex.getMessage();
    }
}
