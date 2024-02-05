package com.cherrydev.cherrymarketbe.server.application.order.event.listener;

import com.cherrydev.cherrymarketbe.server.application.order.event.OrderPlacedEvent;
import com.cherrydev.cherrymarketbe.server.application.order.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j(topic = "orderEventListener")
@RequiredArgsConstructor
public class OrderEventListener {

    private final CartService cartService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("OrderPlacedEvent received: {}", event);
        cartService.clearCartItems(event.getCartIds());
    }


}
