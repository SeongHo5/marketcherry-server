package com.cherrydev.cherrymarketbe.server.domain.order.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RequestCreateOrder(
    @NotNull @NotBlank String orderName,
    @NotNull Boolean getFromMyDefault,
    @Pattern(regexp = "^[가-힣]{2,4}$", message = "수령인 이름은 한글 2~4자로 입력해주세요.") String recipient,
    @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
        String recipientContact,
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리로 입력해주세요.") String zipCode,
    String address,
    String addressDetail,
    @NotNull String deliveryPlace,
    @NotNull String deliveryComment,
    @Nullable Long usedReward) {}
