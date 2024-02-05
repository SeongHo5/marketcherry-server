package com.cherrydev.cherrymarketbe.server.domain.admin.dto.request;

import lombok.Builder;

@Builder
public record GrantCouponByAdmin(String email, String couponCode, String expiredAt) {

}
