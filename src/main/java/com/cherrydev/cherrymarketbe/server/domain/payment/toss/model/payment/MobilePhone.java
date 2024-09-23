package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

public record MobilePhone(
    String customerMobilePhone, SettlementStatus settlementStatus, String receiptUrl) {}
