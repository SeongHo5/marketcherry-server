package com.cherrydev.cherrymarketbe.server.application.payments.controller;

import com.cherrydev.cherrymarketbe.server.application.payments.service.TossPaymentsService;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.CardPromotion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class TossPaymentsController {

    private final TossPaymentsService tossPaymentsService;

    @GetMapping("/{order_code}")
    public TossPayment getPaymentByOrderId(
            @PathVariable("order_code") final String orderCode
    ) {
        return tossPaymentsService.findPaymentByOrderId(orderCode);
    }

    @GetMapping("/{payment_key}")
    @ResponseStatus(HttpStatus.OK)
    public TossPayment getPaymentByPaymentKey(
            @PathVariable("payment_key") final String paymentKey
    ) {
        return tossPaymentsService.findPaymentByPaymentKey(paymentKey);
    }

    /**
     * 결제 승인
     */
    @PostMapping("/{order_code}/approve")
    public ResponseEntity<TossPayment> processPaymentApproval(
            @PathVariable("order_code") String orderCode,
            @RequestParam String paymentKey,
            @RequestParam Number amount
    ) {
        PaymentApproveForm form = new PaymentApproveForm(paymentKey, orderCode, amount);
        return ResponseEntity.ok(tossPaymentsService.processPaymentApproval(form));
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public TossPayment cancelPayment(
            final @RequestParam String paymentKey,
            final @RequestBody PaymentCancelForm form
    ) {
        return tossPaymentsService.cancelPayment(paymentKey, form);
    }

    @GetMapping("/card-promotion")
    @ResponseStatus(HttpStatus.OK)
    public CardPromotion getCardPromotion() {
        return tossPaymentsService.getCardPromotionInfo();
    }


}
