package com.cherrydev.cherrymarketbe.server.application.payments.service;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion.CardPromotion;
import java.util.List;

public interface PaymentService {

  /**
   * 결제 상세정보 생성
   *
   * @param orders 주문 정보
   * @param cartItems 장바구니 정보
   * @param rewardUsed 사용한 리워드
   * @return 결제 상세정보
   */
  PaymentDetail buildPaymentDetail(Orders orders, List<Cart> cartItems, Long rewardUsed);

  /**
   * 결제 승인 정보를 결제 상세정보에 반영
   *
   * @param paymentDetail 결제 상세정보
   * @param tossPayment 결제 정보
   */
  void applyApprovalInfoToDetail(PaymentDetail paymentDetail, TossPayment tossPayment);

  /**
   * 결제 금액 계산
   *
   * @param paymentDetail 결제 상세정보
   * @retur 결제 금액
   */
  Long caculatePaymentAmount(PaymentDetail paymentDetail);

  /**
   * 주문번호로 결제 정보 조회
   *
   * @param orderCode 주문번호
   * @return 결제 정보
   */
  TossPayment findPaymentByOrderId(String orderCode);

  /**
   * 결제키로 결제 정보 조회
   *
   * @param paymentKey 결제키
   * @return 결제 정보
   */
  TossPayment findPaymentByPaymentKey(String paymentKey);

  /**
   * 결제 승인 처리
   *
   * @param form 결제 승인 폼
   * @return 결제 정보
   */
  TossPayment processPaymentApproval(PaymentApproveForm form);

  /**
   * 결제 취소 처리
   *
   * @param paymentKey 결제키
   * @param form 결제 취소 폼
   * @return 결제 정보
   */
  TossPayment cancelPayment(String paymentKey, PaymentCancelForm form);

  /**
   * 카드사 할인 정보 조회
   *
   * @return 카드사 할인 정보
   */
  CardPromotion getCardPromotionInfo();
}
