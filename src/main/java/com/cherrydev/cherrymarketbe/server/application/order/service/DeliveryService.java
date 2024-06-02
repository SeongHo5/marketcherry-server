package com.cherrydev.cherrymarketbe.server.application.order.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cherrydev.cherrymarketbe.server.application.customer.service.AddressService;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.request.RequestCreateOrder;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.DeliveryDetailRepository;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    public static final int DEFAULT_DELIVERY_COST = 3000;
    public static final int MINIMUM_AMOUNT_FOR_FREE_DELIVERY = 30000;

    private final AddressService addressService;
    private final DeliveryDetailRepository deliveryDetailRepository;

    protected DeliveryDetail buildDeliveryDetail(Orders orders, RequestCreateOrder request) {
        boolean getFromMyDefault = request.getFromMyDefault();
        validateRequestFields(request);
        if (getFromMyDefault) {
            CustomerAddress customerAddress = addressService.fetchDefaultAddressForOrders(orders.getAccount());
            return DeliveryDetail.from(orders, customerAddress, request);
        }
        return DeliveryDetail.from(orders, request);
    }

    private void validateRequestFields(RequestCreateOrder request) {
        boolean getFromMe = request.getFromMyDefault();
        boolean hasValueInRecipient = request.recipient() != null;
        if (getFromMe && (!areAllRecipeintFieldsNull(request))) {
            throw new IllegalArgumentException("수령인과 주문자 정보가 같다고 체크했을 때 수령인 정보는 입력할 수 없습니다.");
        }
        if (!getFromMe && !hasValueInRecipient) {
            throw new IllegalArgumentException("수령인 정보는 필수입니다.");
        }
    }

    private boolean areAllRecipeintFieldsNull(RequestCreateOrder request) {
        return request.recipient() == null
                && request.recipientContact() == null
                && request.zipCode() == null
                && request.address() == null
                && request.addressDetail() == null;
    }


}
