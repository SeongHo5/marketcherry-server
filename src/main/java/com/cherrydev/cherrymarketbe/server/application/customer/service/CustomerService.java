package com.cherrydev.cherrymarketbe.server.application.customer.service;

import com.cherrydev.cherrymarketbe.server.application.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.server.application.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerCoupon;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CouponRepository;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CustomerCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.ALREADY_EXIST_COUPON;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.NOT_FOUND_COUPON;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerCouponRepository customerCouponRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void registerCoupon(
            final AccountDetails accountDetails,
            final String couponCode
    ) {
        Account account = accountDetails.getAccount();
        Coupon coupon = fetchCouponEntity(couponCode);
        checkCouponExistence(coupon, account);


        customerCouponRepository.save(CustomerCoupon.from(account, coupon));
    }

    @Transactional(readOnly = true)
    public List<CouponInfo> getCouponList(
            final AccountDetails accountDetails
    ) {
        Account account = accountDetails.getAccount();
        return customerCouponRepository.findAllByAccount(account)
                .stream()
                .map(CustomerCoupon::getCoupon)
                .map(CouponInfo::of)
                .toList();
    }

    // ============ PRIVATE METHODS ============ //

    private Coupon fetchCouponEntity(final String couponCode) {
        return couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUPON));
    }

    private void checkCouponExistence(final Coupon coupon, final Account account) {
        if (customerCouponRepository.existsByAccountAndCoupon(account, coupon)) {
            throw new DuplicatedException(ALREADY_EXIST_COUPON);
        }
    }


}
