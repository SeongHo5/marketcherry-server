package com.cherrydev.cherrymarketbe.server.application.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {

    private final List<Long> cartIds;

    // 주문 Create가 성공했을 때, Cart에 있던 아이템을 모두 지운다.

    public OrderPlacedEvent(Object source, List<Long> cartIds) {
        super(source);
        this.cartIds = cartIds;
    }
}
