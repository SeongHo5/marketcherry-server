package com.cherrydev.cherrymarketbe.server.application.order.service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.GOODS_NOT_AVAILABLE;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.NOT_FOUND_ORDER;

import com.cherrydev.cherrymarketbe.server.application.customer.service.RewardService;
import com.cherrydev.cherrymarketbe.server.application.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.application.order.event.OrderCancelledEvent;
import com.cherrydev.cherrymarketbe.server.application.order.event.OrderPlacedEvent;
import com.cherrydev.cherrymarketbe.server.application.payments.service.PaymentService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsDetailInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.request.RequestCreateOrder;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderCreateResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderInfoResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderSummary;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.OrderDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.domain.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.OrdersRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrdersRepository ordersRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final CartService cartService;
  private final GoodsService goodsService;
  private final RewardService rewardService;
  private final DeliveryService deliveryService;
  private final PaymentService paymentService;

  @Transactional(readOnly = true)
  public Page<OrderInfoResponse> fetchAllMyOrders(
      final AccountDetails accountDetails, Pageable pageable) {
    List<OrderInfoResponse> orders =
        ordersRepository.findAllByAccount(accountDetails.getAccount()).stream()
            .map(OrderInfoResponse::of)
            .toList();
    return new PageImpl<>(orders, pageable, orders.size());
  }

  @Transactional(readOnly = true)
  public OrderSummary fetchOrderDetails(final String orderCode) {
    Orders orders = fetchOrdersEntity(orderCode);

    return handleFetchOrderDetailsInternal(orders);
  }

  @Transactional
  public OrderCreateResponse createOrder(
      final AccountDetails accountDetails, final RequestCreateOrder request) {
    List<Cart> cartItems = cartService.fetchCartItems(accountDetails);
    Orders orders = handleCreateOrderInternal(accountDetails, request, cartItems);
    ordersRepository.save(orders);
    eventPublisher.publishEvent(
        new OrderPlacedEvent(this, cartItems.stream().map(Cart::getId).toList()));
    return buildOrderCreateResponse(orders);
  }

  @Transactional
  public OrderSummary processOrder(
      final String tossPaymentKey, final String orderCode, final Long amount) {
    Orders orders = fetchOrdersEntity(orderCode);
    TossPayment paymentResponse =
        paymentService.processPaymentApproval(
            PaymentApproveForm.of(tossPaymentKey, orderCode, amount));
    paymentService.applyApprovalInfoToDetail(orders.getPaymentDetail(), paymentResponse);
    return handleFetchOrderDetailsInternal(orders);
  }

  @Transactional
  public void cancelOrder(final String orderCode, final PaymentCancelForm cancelForm) {
    Orders orders = fetchOrdersEntity(orderCode);
    String paymentKey = orders.getPaymentDetail().getPaymentKey();

    orders.setStatus(OrderStatus.CANCELLED);
    paymentService.cancelPayment(paymentKey, cancelForm);
    eventPublisher.publishEvent(OrderCancelledEvent.from(this, orders));
  }

  private Orders fetchOrdersEntity(String orderCode) {
    return ordersRepository
        .findByCode(UUID.fromString(orderCode))
        .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
  }

  private OrderSummary handleFetchOrderDetailsInternal(final Orders orders) {
    List<OrderDetail> orderDetail = orders.getOrderDetails();
    PaymentDetail paymentDetail = orders.getPaymentDetail();
    DeliveryDetail deliveryDetail = orders.getDeliveryDetail();

    List<GoodsDetailInfo> goodsDetail =
        orderDetail.stream().map(OrderDetail::getGoods).map(GoodsDetailInfo::of).toList();

    return OrderSummary.from(
        orders.getCode().toString(), paymentDetail, deliveryDetail, goodsDetail);
  }

  @NotNull private Orders handleCreateOrderInternal(
      AccountDetails accountDetails, RequestCreateOrder request, List<Cart> cartItems) {
    // 장바구니에 담긴 상품들이 판매중이고, 재고가 있는지 확인
    List<Cart> availableCartItems = cartService.filterAvailableCartItems(cartItems);

    boolean areAllGoodsAvailable = availableCartItems.size() == cartItems.size();
    if (areAllGoodsAvailable) {
      Orders orders = Orders.from(accountDetails.getAccount(), request.orderName());
      goodsService.updateGoodsInventory(availableCartItems);
      applyRewardUsageIfUsed(accountDetails.getAccount(), request.usedReward());
      orders.setDeliveryDetail(deliveryService.buildDeliveryDetail(orders, request));
      orders.setOrderDetails(
          availableCartItems.stream().map(cart -> OrderDetail.from(orders, cart)).toList());
      orders.setPaymentDetail(
          paymentService.buildPaymentDetail(orders, availableCartItems, request.usedReward()));
      return orders;
    } else {
      throw new ServiceFailedException(GOODS_NOT_AVAILABLE);
    }
  }

  private OrderCreateResponse buildOrderCreateResponse(Orders orders) {
    return new OrderCreateResponse(
        orders.getCode().toString(),
        orders.getName(),
        paymentService.caculatePaymentAmount(orders.getPaymentDetail()));
  }

  private void applyRewardUsageIfUsed(Account account, Long usedReward) {
    if (usedReward != null) {
      rewardService.useReward(account, usedReward.intValue());
    }
  }
}
