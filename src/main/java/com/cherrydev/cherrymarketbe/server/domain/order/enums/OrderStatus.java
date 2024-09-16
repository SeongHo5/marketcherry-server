package com.cherrydev.cherrymarketbe.server.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
  PENDING_PAYMENT("결제 대기"),
  PROCESSING("주문 접수"),
  COMPLETED("배송 완료"),
  CANCELLED("주문 취소"),
  REFUNDED("환불 완료"),
  RETURNED("반품 완료"),
  DELETED("주문 삭제");

  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return this.status;
  }
}
