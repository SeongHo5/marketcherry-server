package com.cherrydev.cherrymarketbe.server.application.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {

    private final List<Long> cartIds;

    public OrderPlacedEvent(Object source, List<Long> cartIds) {
        super(source);
        this.cartIds = cartIds;
    }
}
