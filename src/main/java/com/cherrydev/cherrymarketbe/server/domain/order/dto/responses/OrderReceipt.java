package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;

import java.util.List;

public record OrderReceipt(
        List<GoodsInfo> goodsInfo,
        AmountDetailsInfo amountInfo,
        TossPayment payment
) {
}
