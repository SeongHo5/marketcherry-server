package com.cherrydev.cherrymarketbe.server.application.admin.controller;

import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

public class OrderManagementController {


//    /**
//     * 주문 상태 변경
//     * <p>
//     * 주문 상태가 'COMPLETED' 로 변경될 경우 구매 금액의 10%가 리워드로 지급됩니다.
//     */
//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping("/change-status")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public void updateOrderStatus(
//            final @RequestBody OrderStatusRequest requestChangeDto
//    ) {
//        orderServiceImpl.updateOrderStatus(requestChangeDto);
//    }
//
//
//    @DeleteMapping("/list/{orderCode}/cancel")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> cancelOrder(
//            final @PathVariable String orderCode,
//            final @RequestBody PaymentCancelForm form) {
//
//        orderServiceImpl.cancelOrder(orderCode, form);
//        return ResponseEntity.ok().build();
//    }
}
