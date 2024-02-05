package com.cherrydev.cherrymarketbe.server.application.payments.service;

import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.PaymentDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentDetailRepository paymentDetailRepository;
//
//
//    @Transactional
//    public ResponseEntity<PaymentDetailsInfo> findPaymentDetailsByOrderCode(String orderCode) {
//        return ResponseEntity.ok(paymentDetailRepository.findByOrderCode(orderCode));
//    }
//
//    @Transactional
//    public void createPaymentDetail(
//            AccountDetails accountDetails,
//            OrderReceiptResponse requestDto,
//            TossPayment getOrderWithTossPayment,
//            Long orderId
//    ) {
//        PaymentDetails paymentDetails = new PaymentDetailRequest()
//                .create(
//                        accountDetails,
//                        requestDto,
//                        getOrderWithTossPayment,
//                        orderId
//                );
//        paymentDetailRepository.save(paymentDetails);
//    }
//
//    @Transactional
//    public void cancelPaymentDetail(String orderCode, TossPayment cancelTossPayment) {
//        PaymentDetailsInfo existingPaymentInfo = getOrderWithPaymentDetail(orderCode);
//        PaymentDetails cancelPaymentInfoCreation = new PaymentSummary().update(
//                        orderCode,
//                cancelTossPayment,
//                        existingPaymentInfo
//                );
//       paymentDetailRepository.saveCancelData(cancelPaymentInfoCreation);
//    }
//
//    public PaymentDetailsInfo getOrderWithPaymentDetail(String orderCode) {
//        return  paymentDetailRepository.getByOrderCode(orderCode);
//    }
//

}
