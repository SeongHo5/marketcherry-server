package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model;

import com.cherrydev.cherrymarketbe.server.domain.payment.toss.enums.PaymentMethod;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment.*;
import jakarta.annotation.Nullable;
import java.util.List;

/**
 * Toss Payment PG 결제 정보
 *
 * @param version 응답 버전(날짜 기반)
 * @param paymentKey 결제 고유 키
 * @param type 결제 타입
 * @param orderId 주문 번호
 * @param orderName 구매 상품(e.g., 생수 외 1건)
 * @param mId 상점아이디
 * @param currency 결제에 사용한 통화
 * @param method 결제 수단
 * @param totalAmount 결제 금액
 * @param balanceAmount 취소할 수 있는 금액
 * @param status 결제 상태
 * @param requestedAt 결제 요청 시간(ISO 8601)
 * @param approvedAt 결제 승인 시간(ISO 8601)
 * @param useEscrow 에스크로 사용 여부
 * @param lastTransactionKey 마지막 결제 트랜잭션 키
 * @param suppliedAmount 공급가액
 * @param vat 부가세
 * @param cultureExpense 문화비(도서, 공연 티켓 등) 지출 여부
 * @param taxFreeAmount 면세 금액
 * @param taxExemptionAmount 과세를 제외한 결제 금액
 * @param cancels 결제 취소 이력
 * @param isPartialCancelable 부분 취소 가능 여부
 * @param card 카드로 결제하면 제공되는 카드 관련 정보
 * @param virtualAccount 가상계좌로 결제하면 제공되는 가상계좌 관련 정보
 * @param secret 가상계좌 웹훅이 정상 요청인지 검증하는 값
 * @param mobilePhone 휴대폰으로 결제하면 제공되는 휴대폰 관련 정보
 * @param giftCertificate 상품권으로 결제하면 제공되는 상품권 관련 정보
 * @param transfer 계좌이체로 결제하면 제공되는 계좌이체 관련 정보
 * @param receipt 발행된 영수증 정보
 * @param checkout 결제창 정보
 * @param easyPay 간편결제 정보
 * @param country 결제한 국가(ISO-3166)
 * @param failure 결제 실패 정보
 * @param cashReceipt 현금영수증 정보
 * @param tossDiscount 토스 할인 정보
 * @see PaymentMethod
 * @see Cancel
 * @see Card
 * @see VirtualAccount
 * @see MobilePhone
 * @see GiftCertificate
 * @see Transfer
 * @see Receipt
 * @see Checkout
 * @see EasyPay
 * @see Failure
 * @see CashReceipt
 * @see TossDiscount
 */
public record TossPayment(
    String version,
    String paymentKey,
    PaymentType type,
    String orderId,
    String orderName,
    String mId,
    String currency,
    String method,
    long totalAmount,
    long balanceAmount,
    PaymentStatus status,
    String requestedAt,
    String approvedAt,
    boolean useEscrow,
    @Nullable String lastTransactionKey,
    long suppliedAmount,
    long vat,
    boolean cultureExpense,
    long taxFreeAmount,
    long taxExemptionAmount,
    List<Cancel> cancels,
    boolean isPartialCancelable,
    Card card,
    VirtualAccount virtualAccount,
    @Nullable String secret,
    @Nullable MobilePhone mobilePhone,
    @Nullable GiftCertificate giftCertificate,
    @Nullable Transfer transfer,
    @Nullable Receipt receipt,
    @Nullable Checkout checkout,
    @Nullable EasyPay easyPay,
    String country,
    @Nullable Failure failure,
    @Nullable CashReceipt cashReceipt,
    @Nullable TossDiscount tossDiscount) {}
