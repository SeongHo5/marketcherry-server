package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.OffsetDateTime;

/**
 * 가상계좌 정보입니다.
 *
 * @param accountType 가상계좌 타입을 나타냅니다. "일반", "고정" 중 하나입니다.
 * @param accountNumber 발급된 계좌 번호입니다. 최대 길이는 20자입니다.
 * @param bankCode 가상계좌 은행 숫자 코드입니다. <a
 *     href="https://docs.tosspayments.com/reference/codes#%EC%9D%80%ED%96%89-%EC%BD%94%EB%93%9C">은행
 *     코드</a>를 참고하세요.
 * @param customerName 가상계좌를 발급한 고객 이름입니다. 최대 길이는 100자입니다.
 * @param dueDate 입금 기한입니다.
 * @param refundStatus {@link RefundStatus}
 * @param expired 가상계좌가 만료되었는지 여부입니다.
 * @param settlementStatus {@link SettlementStatus}
 */
public record VirtualAccount(
    String accountType,
    String accountNumber,
    String bankCode,
    String customerName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime dueDate,
    RefundStatus refundStatus,
    Boolean expired,
    SettlementStatus settlementStatus) {}
