package com.cherrydev.cherrymarketbe.server.application.auth.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
@Getter
public class PasswordResetEvent extends ApplicationEvent {

    private final String email;
    public PasswordResetEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
