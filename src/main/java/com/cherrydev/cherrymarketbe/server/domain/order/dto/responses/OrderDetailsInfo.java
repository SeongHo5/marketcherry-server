package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsDetailInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;
import java.util.List;
@Value
@Builder
public class OrderDetailsInfo {

    String orderCode;
    CustomerInfo customerInfo;
    AmountDetailsInfo amountDetailsInfo;
    DeliveryDetailsInfo deliveryDetailsInfo;
    List<GoodsDetailInfo> goodsDetailsInfo;

    @Builder
    public record CustomerInfo(
            String name,
            String contact,
            @JsonFormat(pattern = "yyyy-MM-dd hh:mm:SS")
            Timestamp approveAt
    ) {
        public static CustomerInfo of(PaymentDetail paymentDetail) {
            Account account = paymentDetail.getOrders().getAccount();
            return CustomerInfo.builder()
                    .name(account.getName())
                    .contact(account.getContact())
                    .approveAt(paymentDetail.getPaidAt())
                    .build();
        }

    }

    public static OrderDetailsInfo of(String orderCode, PaymentDetail paymentDetail, DeliveryDetail deliveryDetail, List<GoodsDetailInfo> goodsDetailsInfo) {
        return OrderDetailsInfo.builder()
                .orderCode(orderCode)
                .customerInfo(CustomerInfo.of(paymentDetail))
                .amountDetailsInfo(AmountDetailsInfo.of(paymentDetail))
                .deliveryDetailsInfo(DeliveryDetailsInfo.of(deliveryDetail))
                .goodsDetailsInfo(goodsDetailsInfo)
                .build();
    }

}
