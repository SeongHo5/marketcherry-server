package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

/**
 * 환불 처리 상태입니다. 아래와 같은 상태 값을 가질 수 있습니다.
 *
 * <ol>
 *   <li>NONE: 환불 요청이 없는 상태입니다.
 *   <li>PENDING: 환불을 처리 중인 상태입니다.
 *   <li>FAILED: 환불에 실패한 상태입니다.
 *   <li>PARTIAL_FAILED: 부분 환불에 실패한 상태입니다.
 *   <li>COMPLETED: 환불이 완료된 상태입니다.
 * </ol>
 */
public enum RefundStatus {
  NONE,
  PENDING,
  FAILED,
  PARTIAL_FAILED,
  COMPLETED
}
