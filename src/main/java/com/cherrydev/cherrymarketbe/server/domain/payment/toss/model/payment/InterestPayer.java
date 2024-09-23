package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

/**
 * 무이자 할부가 적용된 결제에서 할부 수수료를 부담하는 주체입니다.
 * <li>BUYER: 상품을 구매한 고객이 할부 수수료를 부담합니다.
 * <li>CARD_COMPANY: 카드사에서 할부 수수료를 부담합니다.
 * <li>MERCHANT: 상점에서 할부 수수료를 부담합니다.
 */
public enum InterestPayer {
  BUYER,
  CARD_COMPANY,
  MERCHANT
}
