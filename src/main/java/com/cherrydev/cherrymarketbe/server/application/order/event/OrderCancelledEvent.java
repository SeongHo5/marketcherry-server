package com.cherrydev.cherrymarketbe.server.application.order.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;

@Getter
public class OrderCancelledEvent extends ApplicationEvent {

    private final Orders orders;
    public OrderCancelledEvent(Object source, Orders orders) {
        super(source);
        this.orders = orders;
    }

    public static OrderCancelledEvent from(Object source, Orders orders) {
        return new OrderCancelledEvent(source, orders);
    }
}
