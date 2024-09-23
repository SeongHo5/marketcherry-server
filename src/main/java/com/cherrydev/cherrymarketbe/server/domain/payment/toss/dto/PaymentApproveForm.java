package com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentApproveForm(
    @NotNull String paymentKey, @NotNull String orderId, @NotNull Number amount) {

  public static PaymentApproveForm of(String paymentKey, String orderId, Number amount) {
    return new PaymentApproveForm(paymentKey, orderId, amount);
  }
}
