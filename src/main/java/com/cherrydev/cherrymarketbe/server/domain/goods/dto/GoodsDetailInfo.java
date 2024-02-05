package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import lombok.Builder;

@Builder
public record GoodsDetailInfo(
        Long goodsId, String goodsCode, String goodsName, String description, Integer price,
        Integer inventory, String storageType, String capacity, String expDate, String allergyInfo,
        String originPlace, String salesStatus, Integer discountRate, Integer discountedPrice,
        String makerName
) {

    public static GoodsDetailInfo of(Goods goods) {
        Discount discount = goods.getDiscount();
        return GoodsDetailInfo.builder()
                .goodsId(goods.getId())
                .goodsCode(goods.getCode())
                .goodsName(goods.getName())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .inventory(goods.getInventory())
                .storageType(goods.getStorageType().toString())
                .capacity(goods.getVolumeType().toString())
                .expDate(goods.getExpireAt())
                .allergyInfo(goods.getAllergyInfo())
                .originPlace(goods.getOriginPlace())
                .salesStatus(goods.getSalesStatus().toString())
                .discountRate(discount.getDiscountRate())
                .discountedPrice((int) (goods.getPrice() * (1 - (discount.getDiscountRate() / 100.0))))
                .makerName(goods.getMaker().getName())
                .build();
    }

}
