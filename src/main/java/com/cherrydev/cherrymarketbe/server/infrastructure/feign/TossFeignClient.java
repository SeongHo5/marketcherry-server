package com.cherrydev.cherrymarketbe.server.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cherrydev.cherrymarketbe.server.application.config.feign.TossFeignConfig;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.CardPromotion;

@FeignClient(name = "tossPayment", url = "https://api.tosspayments.com/v1/payments", configuration = TossFeignConfig.class)
public interface TossFeignClient {

    @GetMapping("/orders/{orderId}")
    TossPayment findPaymentByOrderId(
            @PathVariable("orderId") String orderId
    );

    @GetMapping("/{paymentKey}")
    TossPayment findPaymentByPaymentKey(
            @PathVariable("paymentKey") String paymentKey
    );

    @GetMapping("/v1/promotions/card")
    CardPromotion getCardPromotionInfo();

    @PostMapping("/v1/payments/confirm")
    TossPayment approvePayment(@RequestBody PaymentApproveForm form);

    @PostMapping("/{paymentId}/cancel")
    TossPayment cancelPayment(
            @PathVariable("paymentId") String paymentId,
            @RequestBody PaymentCancelForm form
    );

}
