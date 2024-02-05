package com.cherrydev.cherrymarketbe.server.application.order.service;

import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.DeliveryDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryDetailRepository deliveryDetailRepository;

//    @Transactional
//    public void createDeliveryDetail(AccountDetails accountDetails, Long orderId, String orderCode) {
//        ShippingDetails shippingDetails = new ShippingDetailRequest()
//                .create(
//                        accountDetails,
//                        orderId,
//                        orderCode,
//                        getDefaultAddress(accountDetails)
//                );
//        deliveryDetailRepository.save(shippingDetails);
//    }
//
//    @Transactional
//    public void updateDeliveryDetail(String orderCode, String deliveryStatus) {
//        ShippingStatusRequest requestDto = ShippingStatusRequest.builder()
//                .orderCode(orderCode)
//                .deliveryStatus(deliveryStatus)
//                .build();
//        updateDeliveryDetail(requestDto);
//    }
//
//    @Transactional
//    public void updateDeliveryDetail(ShippingStatusRequest requestDto) {
//        ShippingDetails shippingDetails = requestDto.changeDeliveryStatus();
//        deliveryDetailRepository.updateStatus(shippingDetails);
//    }
//
//    @Transactional
//    public DeliveryDetailsInfo findByOrderCode(String orderCode) {
//        return deliveryDetailRepository.findByOrderCode(orderCode);
//    }
//
//    private CustomerAddress getDefaultAddress(AccountDetails accountDetails) {
//        CustomerAddress address = deliveryDetailRepository.findByDefaultAddress(accountDetails.getAccount().getAccountId());
//
//        if (address.getAddressId() == null) {
//            throw new NotFoundException(NOT_FOUND_ADDRESS);
//        }
//
//
//        return address;
//    }


}
