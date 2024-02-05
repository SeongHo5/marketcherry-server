package com.cherrydev.cherrymarketbe.server.application.customer.controller;

import com.cherrydev.cherrymarketbe.server.application.customer.service.CustomerService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 쿠폰 코드로 쿠폰 등록하기
     *
     * @param couponCode 쿠폰 코드
     */
    @PostMapping("/{coupon_code}/register")
    public ResponseEntity<Void> registerCoupon(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @PathVariable("coupon_code") String couponCode
    ) {
        customerService.registerCoupon(accountDetails, couponCode);
        return ResponseEntity.ok().build();
    }

    /**
     * 내 쿠폰 목록 조회
     */
    @GetMapping("/coupons")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CouponInfo>> getCouponList(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return ResponseEntity.ok(customerService.getCouponList(accountDetails));
    }
}
