package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

/**
 * 카드 정보입니다.
 *
 * @param amount 카드로 결제한 금액
 * @param issuerCode 카드 발급사 코드
 * @param acquirerCode 카드 매입사 코드
 * @param number 카드번호(마스킹된 카드번호)
 * @param installmentPlanMonths 할부 개월 수
 * @param approveNo 카드사 승인 번호
 * @param useCardPoint 카드사 포인트 사용 여부
 * @param cardType 카드 종류
 * @param ownerType 카드 소유자 타입
 * @param acquireStatus 카드 매입 상태
 * @param isInterestFree 무이자 할부 여부
 * @param interestPayer 무이자 할부 지불자
 */
public record Card(
    Long amount,
    String issuerCode,
    String acquirerCode,
    String number,
    Long installmentPlanMonths,
    String approveNo,
    Boolean useCardPoint,
    String cardType,
    String ownerType,
    AcquireStatus acquireStatus,
    Boolean isInterestFree,
    InterestPayer interestPayer) {}
