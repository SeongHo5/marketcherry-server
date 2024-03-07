package com.cherrydev.cherrymarketbe.factory;


import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.IssueCoupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserRole;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserStatus;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;

public class AdminFactory {

    public static ModifyUserRole createModifyUserRoleRequestDtoA() {
        return ModifyUserRole.builder()
                .email("jeongnamgim@example.org")
                .newRole("ROLE_SELLER")
                .build();
    }

    public static ModifyUserStatus createModifyUserStatusByAdminDtoA() {
        return ModifyUserStatus.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .restrictedUntil("2025-12-31")
                .build();
    }

    public static ModifyUserStatus createModifyUserStatusByAdminDtoB() {
        return ModifyUserStatus.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .build();
    }

    public static ModifyUserStatus createModifyUserStatusByAdminDtoC() {
        return ModifyUserStatus.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .restrictedUntil("2020-12-31")
                .build();
    }

    public static RequestAddReward createAddRewardRequestDtoA() {
        return RequestAddReward.builder()
                .email("sungho5527@naver.com")
                .rewardGrantType("ADMIN")
                .amounts(1000)
                .earnedAt("2023-01-01")
                .expiredAt("2024-12-31")
                .build();
    }

    public static RequestAddReward createAddRewardRequestDtoB() {
        return RequestAddReward.builder()
                .email("nothing1515@example.com")
                .rewardGrantType("ADMIN")
                .amounts(1000)
                .earnedAt("2023-01-01")
                .expiredAt("2024-12-31")
                .build();
    }

    public static IssueCoupon createIssueCouponRequestDtoA() {
        return IssueCoupon.builder()
                .code("TEST00")
                .type("OTHER")
                .minimumOrderAmount(10000)
                .discountRate(10)
                .startDate("2021-01-01")
                .endDate("2024-12-31")
                .build();
    }

    public static IssueCoupon createIssueCouponRequestDtoB() {
        return IssueCoupon.builder()
                .code("WELCO0ME")
                .type("OTHER")
                .minimumOrderAmount(10000)
                .discountRate(10)
                .startDate("2021-01-01")
                .endDate("2024-12-31")
                .build();
    }
}
