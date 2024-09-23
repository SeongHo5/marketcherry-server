package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import lombok.Builder;

@Builder
public record PaymentSummary(
    Long totalAmount,
    Long discountedAmount,
    Long couponUsed,
    Long rewardUsed,
    Long deliveryCost,
    String paymentMethods) {
  public static PaymentSummary of(PaymentDetail paymentDetail) {
    return PaymentSummary.builder()
        .totalAmount(paymentDetail.getTotalAmount())
        .discountedAmount(paymentDetail.getDiscountedAmount())
        .couponUsed(paymentDetail.getCouponUsed())
        .rewardUsed(paymentDetail.getRewardUsed())
        .deliveryCost(paymentDetail.getDeliveryCost())
        .paymentMethods(paymentDetail.getPaymentMethods())
        .build();
  }
}
