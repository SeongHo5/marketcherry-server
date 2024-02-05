package com.cherrydev.cherrymarketbe.server.application.order.controller;

import com.cherrydev.cherrymarketbe.server.application.order.service.OrderService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.request.RequestCreateOrder;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderDetailsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<OrderDetailsInfo> fetchOrderDetails(
            @PathVariable("order_code") final String orderCode
    ) {
        return ResponseEntity.ok(
                orderService.fetchOrderDetails(orderCode)
        );
    }

    @PostMapping("/")
    public ResponseEntity<Void> createOrder(
            @AuthenticationPrincipal final AccountDetails accountDetails,
            @RequestBody @Valid final RequestCreateOrder request
    ) {
        orderService.createOrder(accountDetails, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 결제 완료 후 주문 처리
     */
    @PostMapping("/{order_code}/process")
    public void processOrder(
            @AuthenticationPrincipal AccountDetails accountDetails,
            @RequestParam final String tossPaymentKey,
            @PathVariable("order_code") final String orderCode
    ) {
        orderService.processOrder(accountDetails, tossPaymentKey, orderCode);
    }


}
