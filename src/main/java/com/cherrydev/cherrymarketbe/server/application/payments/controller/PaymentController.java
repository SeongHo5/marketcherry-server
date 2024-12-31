package com.cherrydev.cherrymarketbe.server.application.payments.controller;

import com.cherrydev.cherrymarketbe.server.application.payments.service.impl.PaymentServiceImpl;
import com.cherrydev.cherrymarketbe.server.domain.BaseResponse;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.CardPromotion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private final PaymentServiceImpl paymentService;

  @GetMapping("/{orderCode}")
  public ResponseEntity<BaseResponse<TossPayment>> getPaymentByOrderId(
      @PathVariable final String orderCode) {
    TossPayment payment = this.paymentService.findPaymentByOrderId(orderCode);
    return ResponseEntity.ok(BaseResponse.of(payment));
  }

  @GetMapping("/{paymentKey}")
  public ResponseEntity<BaseResponse<TossPayment>> getPaymentByPaymentKey(
      @PathVariable final String paymentKey) {
    TossPayment payment = this.paymentService.findPaymentByPaymentKey(paymentKey);
    return ResponseEntity.ok(BaseResponse.of(payment));
  }

  @PostMapping("/cancel")
  public ResponseEntity<BaseResponse<TossPayment>> cancelPayment(
      final @RequestParam String paymentKey, final @RequestBody PaymentCancelForm form) {
    TossPayment payment = this.paymentService.cancelPayment(paymentKey, form);
    return ResponseEntity.ok(BaseResponse.of(payment));
  }

  @GetMapping("/card-promotion")
  public ResponseEntity<BaseResponse<CardPromotion>> getCardPromotion() {
    CardPromotion cardPromotion = this.paymentService.getCardPromotionInfo();
    return ResponseEntity.ok(BaseResponse.of(cardPromotion));
  }
}
