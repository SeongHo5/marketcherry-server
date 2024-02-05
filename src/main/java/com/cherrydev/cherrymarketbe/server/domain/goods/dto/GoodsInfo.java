package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.Optional;

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

    public static GoodsInfo of(Goods goods, @Nullable Discount discount) {
        // Discount Null Check
        Optional<Discount> optionalDiscount = Optional.ofNullable(discount);
        int discountRate = optionalDiscount.map(Discount::getDiscountRate).orElse(0);
        int discountedPrice = optionalDiscount
                .map(d -> (int) (goods.getPrice() * (1 - d.getDiscountRate() / 100.0)))
                .orElse(goods.getPrice());

        return GoodsInfo.builder()
                .goodsName(goods.getName())
                .goodsCode(goods.getCode())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .discountRate(discountRate)
                .discountedPrice(discountedPrice)
                .build();
    }

}
