package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record CartByStorageType(
        Map<String, List<GoodsInfo>> itemsByType
) {

    public static CartByStorageType of(List<Cart> cart) {
        Map<String, List<GoodsInfo>> itemsByType = cart
                .stream()
                .collect(Collectors.groupingBy(
                        // Map의 key를 storageType으로, value를 GoodsInfo List로 매핑
                        cartItem -> cartItem.getGoods().getStorageType().toString(),
                        Collectors.mapping(
                                cartItem -> GoodsInfo.of(
                                        cartItem.getGoods(),
                                        cartItem.getGoods().getDiscount()
                                ),
                                Collectors.toList()
                        )
                ));
        return new CartByStorageType(itemsByType);
    }

}
