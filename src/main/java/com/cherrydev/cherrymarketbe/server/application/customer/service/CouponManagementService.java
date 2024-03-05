package com.cherrydev.cherrymarketbe.server.application.customer.service;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountQueryService;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.GrantCouponByAdmin;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.IssueCoupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerCoupon;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CouponRepository;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CustomerCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INVALID_INPUT_VALUE;
import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_COUPON;

@Slf4j
@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class CouponManagementService {

    private final AccountQueryService accountQueryService;
    private final CouponRepository couponRepository;
    private final CustomerCouponRepository customerCouponRepository;

    @Transactional
    public void issueCoupon(final IssueCoupon issueCoupon) {
        checkCouponIsValid(issueCoupon.getCode());

        Coupon coupon = Coupon.of(issueCoupon);
        couponRepository.save(coupon);
    }

    @Transactional
    public void grantCoupon(final GrantCouponByAdmin grantCouponByAdmin) {
        Account account = accountQueryService.fetchAccountEntity(grantCouponByAdmin.email());
        Coupon coupon = couponRepository.findByCode(grantCouponByAdmin.couponCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUPON));

        CustomerCoupon customerCoupon = CustomerCoupon.from(account, coupon);

        customerCouponRepository.save(customerCoupon);
    }

    @Transactional(readOnly = true)
    public Page<CouponInfo> getAllCoupons(
            final Pageable pageable
    ) {
        return couponRepository.findAll(pageable)
                .map(CouponInfo::of);
    }

    // =============== PRIVATE METHODS =============== //

    private void checkCouponIsValid(final String code) {
        if (couponRepository.existsByCode(code)) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }
    }
}
