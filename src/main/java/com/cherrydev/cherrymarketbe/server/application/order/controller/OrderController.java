package com.cherrydev.cherrymarketbe.server.application.order.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cherrydev.cherrymarketbe.server.application.order.service.OrderService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.request.RequestCreateOrder;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderCreateResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderInfoResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderSummary;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 목록 조회
     */
    @GetMapping("/me")
    public ResponseEntity<Page<OrderInfoResponse>> fetchAllMyOrders(
            @AuthenticationPrincipal AccountDetails accountDetails,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                orderService.fetchAllMyOrders(accountDetails, pageable)
        );
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/me/{order_code}/detail")
    public ResponseEntity<OrderSummary> fetchOrderDetails(
            @PathVariable("order_code") final String orderCode
    ) {
        return ResponseEntity.ok(
                orderService.fetchOrderDetails(orderCode)
        );
    }

    /**
     * 주문 생성
     *
     * @param accountDetails 로그인한 사용자 정보
     * @param request        주문 생성 요청
     */
    @PostMapping("")
    public ResponseEntity<OrderCreateResponse> createOrder(
            @AuthenticationPrincipal final AccountDetails accountDetails,
            @RequestBody @Valid final RequestCreateOrder request
    ) {
        return ResponseEntity.status(CREATED)
                .body(orderService.createOrder(accountDetails, request));
    }

    /**
     * 결제 승인 및 주문 처리
     */
    @PostMapping("/{order_code}/process")
    public ResponseEntity<OrderSummary> processOrder(
            @PathVariable("order_code") final String orderCode,
            @RequestParam final String tossPaymentKey,
            @RequestParam final Long amount
    ) {
        return ResponseEntity.status(CREATED)
                .body(orderService.processOrder(tossPaymentKey, orderCode, amount));
    }

    /**
     * 주문 취소
     */
    @DeleteMapping("/{order_code}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable("order_code") final String orderCode,
            @RequestBody final PaymentCancelForm cancelForm
    ) {
        orderService.cancelOrder(orderCode, cancelForm);
        return ResponseEntity.ok().build();
    }


}
