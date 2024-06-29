package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import java.sql.Timestamp;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;

import com.fasterxml.jackson.annotation.JsonFormat;
@Builder
public record OrderInfoResponse(
        @NotNull String orderCode,
        @NotNull String orderStatus,
        @NotNull Integer amount,
        @NotNull String orderName,
        @NotNull String paymentMethod,
        @NotNull String goodsCode,
        @JsonFormat(pattern = "yyyy.MM.dd (HH시 mm분)")
        @NotNull Timestamp createdAt

) {

    public static OrderInfoResponse of(Orders orders) {
        return OrderInfoResponse.builder()
                .orderCode(orders.getCode().toString())
                .orderStatus(orders.getStatus().toString())
                .orderName(orders.getName())
                .createdAt(orders.getCreatedAt())
                .build();
    }



}
