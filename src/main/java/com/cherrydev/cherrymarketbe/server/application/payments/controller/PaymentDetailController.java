package com.cherrydev.cherrymarketbe.server.application.payments.controller;

import com.cherrydev.cherrymarketbe.server.application.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order/payment-detail")
public class PaymentDetailController {

    private final PaymentService paymentService;

//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/{orderCode}")
//    public ResponseEntity<PaymentDetailsInfo> getPaymentDetailsByOrderCode(@PathVariable String orderCode) {
//        return paymentService.findPaymentDetailsByOrderCode(orderCode);
//    }

}
