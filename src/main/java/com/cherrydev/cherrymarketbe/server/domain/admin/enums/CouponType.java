package com.cherrydev.cherrymarketbe.server.domain.admin.enums;

import lombok.Getter;

@Getter
public enum CouponType {
  DISCOUNT,
  FREE_SHIPPING,
  FIRST_ORDER_DISCOUNT,
  CATEGORY_DISCOUNT,
  EVENT,
  ROYALTY,
  BIRTHDAY,
  WELCOME,
  RECOMMENDATION,
  OTHER;
}
