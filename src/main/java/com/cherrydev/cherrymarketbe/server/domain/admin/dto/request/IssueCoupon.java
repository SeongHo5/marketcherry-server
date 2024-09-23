package com.cherrydev.cherrymarketbe.server.domain.admin.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IssueCoupon {

  @Pattern(regexp = "^[A-Z0-9]{8}$", message = "쿠폰 코드는 8자리의 영문 대문자와 숫자로 이루어져야 합니다.")
  String code;

  String type;

  Integer minimumOrderAmount;

  @Pattern(regexp = "^\\d{1,2}$", message = "할인율은 1 ~ 99 사이의 숫자로 입력해주세요.")
  Integer discountRate;

  String startDate;

  String endDate;
}
