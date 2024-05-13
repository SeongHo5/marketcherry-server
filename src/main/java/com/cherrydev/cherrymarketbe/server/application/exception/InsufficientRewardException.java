package com.cherrydev.cherrymarketbe.server.application.exception;

import lombok.Getter;

@Getter
public class InsufficientRewardException extends ApplicationException {

    public InsufficientRewardException(ExceptionStatus status) {
        super(status);
    }
}
