package com.cherrydev.cherrymarketbe.server.application.order.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.application.order.event.OrderPlacedEvent;
import com.cherrydev.cherrymarketbe.server.application.payments.service.TossPaymentsService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsDetailInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.request.RequestCreateOrder;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderDetailsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderInfoResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.OrderDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.OrdersRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.GOODS_NOT_AVAILABLE;
import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_ORDER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TossPaymentsService tossPaymentsService;
    private final CartService cartService;
    private final GoodsService goodsService;

    @Transactional(readOnly = true)
    public Page<OrderInfoResponse> fetchAllMyOrders(
            final AccountDetails accountDetails,
            Pageable pageable
    ) {
        List<OrderInfoResponse> orders = ordersRepository.findAllByAccount(accountDetails.getAccount())
                .stream()
                .map(OrderInfoResponse::of)
                .toList();
        return new PageImpl<>(orders, pageable, orders.size());
    }

    @Transactional(readOnly = true)
    public OrderDetailsInfo fetchOrderDetails(
            final String orderCode
    ) {
        Orders orders = fetchOrdersEntity(orderCode);

        return handleFetchOrderDetailsInternal(orders);
    }

    @Transactional
    public void createOrder(
            final AccountDetails accountDetails,
            final RequestCreateOrder request
    ) {
        List<Cart> cartItems = cartService.fetchCartItems(accountDetails);
        Orders orders = handleCreateOrderInternal(accountDetails, request, cartItems);
        ordersRepository.save(orders);
        eventPublisher.publishEvent(new OrderPlacedEvent(this, cartItems.stream().map(Cart::getId).toList()));
    }

    @NotNull
    private Orders handleCreateOrderInternal(
            AccountDetails accountDetails,
            RequestCreateOrder request,
            List<Cart> cartItems
    ) {
        // 장바구니에 담긴 상품들이 판매중이고, 재고가 있는지 확인
        List<Cart> availableCartItems = cartItems.stream()
                .filter(cart -> cart.getGoods().getSalesStatus().isOnSale())
                .toList();
        List<Cart> validCartItems = availableCartItems.stream()
                .filter(cart -> cart.getQuantity() <= cart.getGoods().getInventory())
                .toList();

        boolean isAllGoodsAvailable = validCartItems.size() == cartItems.size();
        if (isAllGoodsAvailable) {
            Orders orders = Orders.of(accountDetails.getAccount(), request.orderName());
            goodsService.updateGoodsInventory(validCartItems);
            orders.setDeliveryDetail(DeliveryDetail.of(orders, request));
            orders.setOrderDetails(validCartItems.stream().map(cart -> OrderDetail.of(orders, cart)).toList());
            return orders;
        } else {
            throw new ServiceFailedException(GOODS_NOT_AVAILABLE);
        }
    }

    @Transactional
    public void processOrder(
            final String tossPaymentKey,
            final String orderCode
    ) {
        Orders orders = fetchOrdersEntity(orderCode);
        TossPayment tossPayment = tossPaymentsService.findPaymentByPaymentKey(tossPaymentKey);
        PaymentDetail.of(orders, tossPayment);
    }

    private Orders fetchOrdersEntity(String orderCode) {
        return ordersRepository.findByCode(UUID.fromString(orderCode))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
    }

    private OrderDetailsInfo handleFetchOrderDetailsInternal(final Orders orders) {
        List<OrderDetail> orderDetail = orders.getOrderDetails();
        PaymentDetail paymentDetail = orders.getPaymentDetail();
        DeliveryDetail deliveryDetail = orders.getDeliveryDetail();

        List<GoodsDetailInfo> goodsDetail = orderDetail
                .stream()
                .map(OrderDetail::getGoods)
                .map(GoodsDetailInfo::of)
                .toList();

        return OrderDetailsInfo.of(orders.getCode().toString(), paymentDetail, deliveryDetail, goodsDetail);
    }

}
