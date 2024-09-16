package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

/**
 * 이체 정보입니다.
 *
 * @param bankCode 은행 코드입니다. <a
 *     href="https://docs.tosspayments.com/reference/codes#%EC%9D%80%ED%96%89-%EC%BD%94%EB%93%9C">은행
 *     코드</a>를 참고하세요.
 * @param settlementStatus {@link SettlementStatus}
 */
public record Transfer(String bankCode, SettlementStatus settlementStatus) {}
