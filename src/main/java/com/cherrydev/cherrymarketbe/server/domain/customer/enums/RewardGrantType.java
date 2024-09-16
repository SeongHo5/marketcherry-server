package com.cherrydev.cherrymarketbe.server.domain.customer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RewardGrantType {
  WELCOME("회원가입"),
  PURCHASE("구매"),
  REVIEW("리뷰"),
  EVENT("이벤트"),
  USE("사용"),
  ADMIN("관리자");

  private final String description;

  public boolean isSameType(RewardGrantType type) {
    return this == type;
  }
}
