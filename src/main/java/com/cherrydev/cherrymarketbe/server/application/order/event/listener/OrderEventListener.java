package com.cherrydev.cherrymarketbe.server.application.order.event.listener;

import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsInventoryService;
import com.cherrydev.cherrymarketbe.server.application.order.event.OrderCancelledEvent;
import com.cherrydev.cherrymarketbe.server.application.order.event.OrderPlacedEvent;
import com.cherrydev.cherrymarketbe.server.application.order.service.CartService;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.OrderDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import java.util.List;
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
  private final GoodsInventoryService inventoryService;

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(
      classes = OrderPlacedEvent.class,
      phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderPlacedEvent(OrderPlacedEvent event) {
    log.info("OrderPlacedEvent received!");
    cartService.clearCartItems(event.getCartIds());
  }

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(
      classes = OrderCancelledEvent.class,
      phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderCancelledEvent(OrderCancelledEvent event) {
    Orders orders = event.getOrders();
    log.info("OrderCancelledEvent received!");
    restoreGoodsInventory(orders.getOrderDetails());
  }

  private void restoreGoodsInventory(List<OrderDetail> orderDetails) {
    orderDetails.forEach(
        orderDetail ->
            inventoryService.processInventoryUpdate(
                orderDetail.getGoods(), orderDetail.getOrderQuantity()));
  }
}
