package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import java.util.List;

import lombok.Builder;
import lombok.Value;

import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsDetailInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
@Value
@Builder
public class OrderSummary {

    String orderCode;
    PaymentSummary paymentSummary;
    DeliverySummary deliverySummary;
    List<GoodsDetailInfo> orderedGoods;


    public static OrderSummary from(String orderCode, PaymentDetail paymentDetail, DeliveryDetail deliveryDetail, List<GoodsDetailInfo> goodsDetailsInfo) {
        return OrderSummary.builder()
                .orderCode(orderCode)
                .paymentSummary(PaymentSummary.of(paymentDetail))
                .deliverySummary(DeliverySummary.of(deliveryDetail))
                .orderedGoods(goodsDetailsInfo)
                .build();
    }

}
