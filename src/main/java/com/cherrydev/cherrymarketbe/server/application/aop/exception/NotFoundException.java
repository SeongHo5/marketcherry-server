package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends ApplicationException {

    public NotFoundException(ExceptionStatus status) {
        super(status);
    }
}
