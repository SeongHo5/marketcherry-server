package com.cherrydev.cherrymarketbe.server.application.payments.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cherrydev.cherrymarketbe.server.application.payments.service.PaymentService;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.CardPromotion;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{order_code}")
    public TossPayment getPaymentByOrderId(
            @PathVariable("order_code") final String orderCode
    ) {
        return paymentService.findPaymentByOrderId(orderCode);
    }

    @GetMapping("/{payment_key}")
    @ResponseStatus(HttpStatus.OK)
    public TossPayment getPaymentByPaymentKey(
            @PathVariable("payment_key") final String paymentKey
    ) {
        return paymentService.findPaymentByPaymentKey(paymentKey);
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public TossPayment cancelPayment(
            final @RequestParam String paymentKey,
            final @RequestBody PaymentCancelForm form
    ) {
        return paymentService.cancelPayment(paymentKey, form);
    }

    @GetMapping("/card-promotion")
    @ResponseStatus(HttpStatus.OK)
    public CardPromotion getCardPromotion() {
        return paymentService.getCardPromotionInfo();
    }


}
