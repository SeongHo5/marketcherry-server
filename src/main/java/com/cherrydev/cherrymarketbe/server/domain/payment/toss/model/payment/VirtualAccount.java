package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAccount {
    /**
     * 가상계좌 타입을 나타냅니다. "일반", "고정" 중 하나입니다.
     */
    @JsonProperty("accountType")
    private String accountType;

    /**
     * 발급된 계좌 번호입니다. 최대 길이는 20자입니다.
     */
    @JsonProperty("accountNumber")
    private String accountNumber;

    /**
     * 가상계좌 은행 숫자 코드입니다. [은행 코드](https://docs.tosspayments.com/reference/codes#%EC%9D%80%ED%96%89-%EC%BD%94%EB%93%9C)를 참고하세요.
     *
     */
    @JsonProperty("bankCode")
    private String bankCode;

    /**
     * 가상계좌를 발급한 고객 이름입니다. 최대 길이는 100자입니다.
     */
    @JsonProperty("customerName")
    private String customerName;

    /**
     * 입금 기한입니다.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonProperty("dueDate")
    private OffsetDateTime dueDate;

    @JsonProperty("refundStatus")
    private RefundStatus refundStatus;

    /**
     * 가상계좌가 만료되었는지 여부입니다.
     */
    @JsonProperty("expired")
    private boolean expired;

    @JsonProperty("settlementStatus")
    private SettlementStatus settlementStatus;

    // Ensure that the RefundStatus and SettlementStatus classes or enums are also defined in your Java codebase
}
