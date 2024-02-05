package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class DuplicatedException extends ApplicationException {

    public DuplicatedException(ExceptionStatus status) {
        super(status);
    }
}
