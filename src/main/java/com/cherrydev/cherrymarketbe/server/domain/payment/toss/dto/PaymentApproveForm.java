package com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentApproveForm(
        @NotNull String paymentKey,
        @NotNull String orderId,
        @NotNull Number amount
) {

}
