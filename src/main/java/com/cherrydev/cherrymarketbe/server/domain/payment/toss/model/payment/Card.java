package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    /**
     * 카드로 결제한 금액입니다.
     */
    @JsonProperty("amount")
    private long amount;

    /**
     * 카드 발급사 숫자 코드입니다. [카드사 코드](https://docs.tosspayments.com/reference/codes#%EC%B9%B4%EB%93%9C%EC%82%AC-%EC%BD%94%EB%93%9C)를 참고하세요.
     */
    @JsonProperty("issuerCode")
    private String issuerCode;

    /**
     * 카드 매입사 숫자 코드입니다. [카드사 코드](https://docs.tosspayments.com/reference/codes#%EC%B9%B4%EB%93%9C%EC%82%AC-%EC%BD%94%EB%93%9C)를 참고하세요.
     */
    @JsonProperty("acquirerCode")
    private String acquirerCode;

    public String getAcquirerCode() {
        return AcquireCode.getDescriptionByCode(this.acquirerCode);
    }

    /**
     * 카드번호입니다. 번호의 일부는 마스킹 되어 있습니다. 최대 길이는 20자입니다.
     */
    @JsonProperty("number")
    private String number;

    /**
     * 할부 개월 수입니다. 일시불이면 0입니다.
     */
    @JsonProperty("installmentPlanMonths")
    private long installmentPlanMonths;

    /**
     * 카드사 승인 번호입니다. 최대 길이는 8자입니다.
     */
    @JsonProperty("approveNo")
    private String approveNo;

    /**
     * 카드사 포인트를 사용했는지 여부입니다.
     */
    @JsonProperty("useCardPoint")
    private boolean useCardPoint;

    /**
     * 카드 종류입니다. "신용", "체크", "기프트" 중 하나입니다.
     */
    @JsonProperty("cardType")
    private String cardType;

    /**
     * 카드의 소유자 타입입니다. "개인", "법인" 중 하나입니다.
     */
    @JsonProperty("ownerType")
    private String ownerType;

    @JsonProperty("acquireStatus")
    private AcquireStatus acquireStatus;

    /**
     * 무이자 할부의 적용 여부입니다.
     */
    @JsonProperty("isInterestFree")
    private boolean isInterestFree;

    @JsonProperty("interestPayer")
    private InterestPayer interestPayer;

}
