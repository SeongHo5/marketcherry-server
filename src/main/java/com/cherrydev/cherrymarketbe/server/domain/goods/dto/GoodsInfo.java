package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import lombok.Builder;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;

import com.fasterxml.jackson.annotation.JsonInclude;

@Builder
public record GoodsInfo(
        String goodsName,
        String goodsCode,
        String description,
        Integer price,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer discountRate,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer discountedPrice
) {

    public static GoodsInfo of(Goods goods) {
        return GoodsInfo.builder()
                .goodsName(goods.getName())
                .goodsCode(goods.getCode())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .discountRate(goods.getDiscountRate())
                .discountedPrice(goods.getDiscountedPrice())
                .build();
    }

}
