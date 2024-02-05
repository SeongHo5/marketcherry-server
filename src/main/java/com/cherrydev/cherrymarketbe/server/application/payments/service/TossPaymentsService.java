package com.cherrydev.cherrymarketbe.server.application.payments.service;

import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.*;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TossPaymentsService {

    public static final String PAYMENT_APPROVAL = "APPROVED::";
    public static final String PAYMENT_CANCEL = "CANCELED::";
    public static final String PAYMENT_KEY_SEPARATOR = "::";

    private final TossFeignClient tossFeignClient;
    private final RedisService redisService;

    /**
     * 주문 번호로 결제 조회
     * @param orderCode 주문 번호
     */
    public TossPayment findPaymentByOrderId(String orderCode) {
        return tossFeignClient.findPaymentByOrderId(orderCode);
    }

    /**
     * 결제 고유 번호로 결제 조회
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
        TossPayment tossPaymentInfo = tossFeignClient.approvePayment(form);
        cachePaymentInfo(tossPaymentInfo, PAYMENT_APPROVAL);
        return tossPaymentInfo;
    }

    /**
     * 결제 취소
     * @param paymentKey 결제 고유 번호
     * @param form 취소 정보
     * @return Payment
     */
    @Transactional
    public TossPayment cancelPayment(String paymentKey, PaymentCancelForm form) {
        TossPayment tossPaymentInfo = tossFeignClient.cancelPayment(paymentKey, form);
        cachePaymentInfo(tossPaymentInfo, PAYMENT_CANCEL);
        return tossPaymentInfo;
    }

    /**
     * 카드 혜택 조회
     */
    public CardPromotion getCardPromotionInfo() {
        return tossFeignClient.getCardPromotionInfo();
    }


    private void cachePaymentInfo(TossPayment tossPaymentInfo, String status) {
        String key = status + tossPaymentInfo.getPaymentKey();
        redisService.setData(key, String.valueOf(tossPaymentInfo));
    }

}
