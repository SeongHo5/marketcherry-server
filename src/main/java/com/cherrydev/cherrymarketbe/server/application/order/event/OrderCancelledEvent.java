package com.cherrydev.cherrymarketbe.server.application.order.event;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderCancelledEvent extends ApplicationEvent {

    private final Orders orders;
    public OrderCancelledEvent(Object source, Orders orders) {
        super(source);
        this.orders = orders;
    }
}
