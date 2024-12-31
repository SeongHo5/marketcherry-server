package com.cherrydev.cherrymarketbe.server.application.payments.service.impl;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import java.util.concurrent.atomic.AtomicLong;

class PaymentCalculator {

  private final AtomicLong totalAmount = new AtomicLong(0);
  private final AtomicLong discountAmount = new AtomicLong(0);

  void addCartToTotal(Cart cart) {
    long price = cart.getGoods().getPrice();
    long quantity = cart.getQuantity();
    long discountRate = cart.getGoods().getDiscountRate();

    this.totalAmount.addAndGet(price * quantity);
    this.discountAmount.addAndGet(price * quantity * discountRate / 100);
  }

  PaymentCalculator mergeWith(PaymentCalculator other) {
    this.totalAmount.addAndGet(other.totalAmount.get());
    this.discountAmount.addAndGet(other.discountAmount.get());
    return this;
  }

  public long getTotalAmount() {
    return this.totalAmount.get();
  }

  public long getDiscountAmount() {
    return this.discountAmount.get();
  }
}
