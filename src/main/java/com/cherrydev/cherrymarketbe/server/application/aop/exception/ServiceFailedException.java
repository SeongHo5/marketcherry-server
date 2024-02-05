package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class ServiceFailedException extends ApplicationException {

    public ServiceFailedException(ExceptionStatus ex) {
        super(ex);
    }
}
