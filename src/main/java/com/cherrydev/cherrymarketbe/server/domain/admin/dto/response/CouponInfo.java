package com.cherrydev.cherrymarketbe.server.domain.admin.dto.response;

import lombok.Builder;
import lombok.Value;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.enums.CouponType;

@Value
@Builder
public class CouponInfo {

    String code;

    CouponType type;

    Integer minimumOrderAmount;

    Integer discountRate;

    String startDate;

    String endDate;

    public static CouponInfo of(Coupon coupon) {
        return CouponInfo.builder()
                .code(coupon.getCode())
                .type(coupon.getType())
                .minimumOrderAmount(coupon.getMinimumAmount())
                .discountRate(coupon.getDiscountRate())
                .startDate(coupon.getStartDate().toString())
                .endDate(coupon.getEndDate().toString())
                .build();
    }
}
