package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GoodsInventoryResponse {

    Long goodsId;

    String salesStatus;

    int inventory;
}
