package com.cherrydev.cherrymarketbe.server.application.payments.service.impl;

import static com.cherrydev.cherrymarketbe.server.application.order.service.DeliveryService.DEFAULT_DELIVERY_COST;
import static com.cherrydev.cherrymarketbe.server.application.order.service.DeliveryService.MINIMUM_AMOUNT_FOR_FREE_DELIVERY;

import com.cherrydev.cherrymarketbe.server.application.payments.service.PaymentService;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.server.infrastructure.feign.TossFeignClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final TossFeignClient tossFeignClient;

  @Override
  public PaymentDetail buildPaymentDetail(Orders orders, List<Cart> cartItems, Long rewardUsed) {
    PaymentCalculator calculation =
        cartItems.stream()
            .reduce(
                new PaymentCalculator(),
                (calculator, cart) -> {
                  calculator.addCartToTotal(cart);
                  return calculator;
                },
                PaymentCalculator::mergeWith);

    final long totalAmount = calculation.getTotalAmount();
    final long discountAmount = calculation.getDiscountAmount();
    final long deliveryCost =
        calculation.getTotalAmount() < MINIMUM_AMOUNT_FOR_FREE_DELIVERY
            ? DEFAULT_DELIVERY_COST
            : 0L;
    final long paymentAmount = totalAmount - discountAmount + deliveryCost;

    return PaymentDetail.from(orders, totalAmount, discountAmount, paymentAmount, rewardUsed);
  }

  @Override
  public void applyApprovalInfoToDetail(PaymentDetail paymentDetail, TossPayment tossPayment) {
    paymentDetail.applyApprovalInfo(tossPayment);
  }

  @Override
  public Long caculatePaymentAmount(PaymentDetail paymentDetail) {
    return paymentDetail.getTotalAmount()
        - paymentDetail.getDiscountedAmount()
        + paymentDetail.getDeliveryCost();
  }

  @Override
  public TossPayment findPaymentByOrderId(String orderCode) {
    return tossFeignClient.findPaymentByOrderId(orderCode);
  }

  @Override
  public TossPayment findPaymentByPaymentKey(String paymentKey) {
    return tossFeignClient.findPaymentByPaymentKey(paymentKey);
  }

  @Override
  public TossPayment processPaymentApproval(PaymentApproveForm form) {
    return tossFeignClient.approvePayment(form);
  }

  @Override
  public TossPayment cancelPayment(String paymentKey, PaymentCancelForm form) {
    return tossFeignClient.cancelPayment(paymentKey, form);
  }

  @Override
  public CardPromotion getCardPromotionInfo() {
    return tossFeignClient.getCardPromotionInfo();
  }
}
