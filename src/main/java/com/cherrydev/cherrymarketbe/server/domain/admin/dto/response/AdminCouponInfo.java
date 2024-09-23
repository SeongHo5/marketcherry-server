package com.cherrydev.cherrymarketbe.server.domain.admin.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.admin.enums.CouponType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AdminCouponInfo {

  String code;

  CouponType type;

  Integer minimumOrderAmount;

  Integer discountAmount;

  String startDate;

  String endDate;
}
