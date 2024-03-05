package com.cherrydev.cherrymarketbe.server.application.payments.service;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.server.infrastructure.feign.TossFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.order.service.DeliveryService.DEFAULT_DELIVERY_COST;
import static com.cherrydev.cherrymarketbe.server.application.order.service.DeliveryService.MINIMUM_AMOUNT_FOR_FREE_DELIVERY;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TossFeignClient tossFeignClient;

    @Transactional
    public PaymentDetail buildPaymentDetail(Orders orders, List<Cart> cartItems, Long rewardUsed) {
        PaymentCalculator calculation = cartItems.stream().reduce(new PaymentCalculator(), (acc, cart) -> {
            acc.accumulate(cart);
            return acc;
        }, PaymentCalculator::combine);

        long totalAmount = calculation.getTotalAmount();
        long discountAmount = calculation.getDiscountAmount();
        long deliveryCost = calculation.getTotalAmount() < MINIMUM_AMOUNT_FOR_FREE_DELIVERY ? DEFAULT_DELIVERY_COST : 0L;
        long paymentAmount = totalAmount - discountAmount + deliveryCost;

        return PaymentDetail.from(orders, totalAmount, discountAmount, paymentAmount, rewardUsed);
    }

    @Transactional
    public void applyApprovalInfoToDetail(PaymentDetail paymentDetail, TossPayment tossPayment) {
        paymentDetail.applyApprovalInfo(tossPayment);
    }

    public Long caculatePaymentAmount(PaymentDetail paymentDetail) {
        return paymentDetail.getTotalAmount() - paymentDetail.getDiscountedAmount() + paymentDetail.getDeliveryCost();
    }

    /**
     * 주문 번호로 결제 조회
     *
     * @param orderCode 주문 번호
     */
    public TossPayment findPaymentByOrderId(String orderCode) {
        return tossFeignClient.findPaymentByOrderId(orderCode);
    }

    /**
     * 결제 고유 번호로 결제 조회
     *
     * @param paymentKey 결제 고유 번호
     */
    public TossPayment findPaymentByPaymentKey(String paymentKey) {
        return tossFeignClient.findPaymentByPaymentKey(paymentKey);
    }

    /**
     * 결제 승인
     */
    @Transactional
    public TossPayment processPaymentApproval(PaymentApproveForm form) {
        return tossFeignClient.approvePayment(form);
    }

    /**
     * 결제 취소
     *
     * @param paymentKey 결제 고유 번호
     * @param form       취소 정보
     * @return Payment
     */
    @Transactional
    public TossPayment cancelPayment(String paymentKey, PaymentCancelForm form) {
        return tossFeignClient.cancelPayment(paymentKey, form);
    }

    /**
     * 카드 혜택 조회
     */
    public CardPromotion getCardPromotionInfo() {
        return tossFeignClient.getCardPromotionInfo();
    }

}
