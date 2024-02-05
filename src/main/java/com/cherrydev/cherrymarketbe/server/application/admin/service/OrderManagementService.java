package com.cherrydev.cherrymarketbe.server.application.admin.service;

import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public class OrderManagementService {
//    @Transactional
//    public void updateOrderStatus(OrderStatusRequest requestDto) {
//
//        Order order = requestDto.changeOrderStatus();
//
//        if (requestDto.orderStatus().equals("COMPLETED")) {
//            String accountEmail = ordersRepository.findAccountEmailByOrderCode(order.getOrderCode());
//            Integer orderAmount = ordersRepository.findAmountByOrderCode(order.getOrderCode());
//
//            RequestAddReward rewardRequestDto = RequestAddReward.builder()
//                    .email(accountEmail)
//                    .rewardGrantType("PURCHASE")
//                    .amounts((int) (Math.round(orderAmount * 0.1)))
//                    .earnedAt(String.valueOf(LocalDate.now()))
//                    .expiredAt((LocalDate.now()).plusMonths(6).toString())
//                    .build();
//            rewardService.grantReward(rewardRequestDto);
//        }
//
//        ordersRepository.updateStatus(order);
//    }
}
