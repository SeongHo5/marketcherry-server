package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import java.time.OffsetDateTime;

public record Cancel(
    Long cancelAmount,
    String cancelReason,
    Long taxFreeAmount,
    Long taxExemptionAmount,
    Long refundableAmount,
    Long easyPayDiscountAmount,
    OffsetDateTime canceledAt,
    String transactionKey) {}
