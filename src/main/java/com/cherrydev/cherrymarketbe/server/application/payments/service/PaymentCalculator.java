package com.cherrydev.cherrymarketbe.server.application.payments.service;

import lombok.Getter;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;

@Getter
class PaymentCalculator {

    protected long totalAmount = 0;
    protected long discountAmount = 0;

    void accumulate(Cart cart) {
        long price = cart.getGoods().getPrice();
        long quantity = cart.getQuantity();
        long discountRate = cart.getGoods().getDiscountRate();

        totalAmount += price * quantity;
        discountAmount += price * quantity * discountRate / 100;
    }

    PaymentCalculator combine(PaymentCalculator other) {
        totalAmount += other.totalAmount;
        discountAmount += other.discountAmount;
        return this;
    }
}
