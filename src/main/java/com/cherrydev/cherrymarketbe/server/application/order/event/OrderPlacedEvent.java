package com.cherrydev.cherrymarketbe.server.application.order.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {

    private final List<Long> cartIds;

    public OrderPlacedEvent(Object source, List<Long> cartIds) {
        super(source);
        this.cartIds = cartIds;
    }
}
