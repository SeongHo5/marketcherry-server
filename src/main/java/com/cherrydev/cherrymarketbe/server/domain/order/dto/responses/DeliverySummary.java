package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import lombok.Builder;

@Builder
public record DeliverySummary(

        String shippingStatus,
        String recipient,
        String recipientContact,
        String zipCode,
        String address,
        String addressDetail,
        String place,
        String request
) {
    public static DeliverySummary of(DeliveryDetail deliveryDetail) {
        return DeliverySummary.builder()
                .shippingStatus(deliveryDetail.getDeliveryStatus().getStatus())
                .recipient(deliveryDetail.getRecipient())
                .recipientContact(deliveryDetail.getRecipientContact())
                .zipCode(deliveryDetail.getZipCode())
                .address(deliveryDetail.getRoadNameAddress())
                .addressDetail(deliveryDetail.getAddressDetail())
                .place(deliveryDetail.getDeliveryPlace())
                .request(deliveryDetail.getDeliveryRequest())
                .build();
    }
}
