package com.cherrydev.cherrymarketbe.server.application.exception;

import lombok.Getter;

@Getter
public class ServiceFailedException extends ApplicationException {

    public ServiceFailedException(ExceptionStatus ex) {
        super(ex);
    }
}
