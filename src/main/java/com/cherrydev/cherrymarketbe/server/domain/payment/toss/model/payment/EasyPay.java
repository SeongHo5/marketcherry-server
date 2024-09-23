package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

public record EasyPay(String provider, Long amount, Long discountAmount) {}
