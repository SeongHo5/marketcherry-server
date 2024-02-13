package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class InsufficientRewardException extends ApplicationException {

    public InsufficientRewardException(ExceptionStatus status) {
        super(status);
    }
}
