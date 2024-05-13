package com.cherrydev.cherrymarketbe.server.application.exception;

import lombok.Getter;

@Getter
public class CouldNotObtainLockException extends RuntimeException {

    public CouldNotObtainLockException(String message) {
        super(message);
    }
}
