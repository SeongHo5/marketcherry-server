package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCard {
    /**
     * 카드사 숫자 코드입니다. [카드사 코드](https://docs.tosspayments.com/reference/codes#%EC%B9%B4%EB%93%9C%EC%82%AC-%EC%BD%94%EB%93%9C)를 참고하세요.
     */
    @JsonProperty("companyCode")
    private String companyCode;

    /**
     * 할인 금액입니다.
     */
    @JsonProperty("discountAmount")
    private long discountAmount;

    /**
     * 남은 프로모션 예산입니다. 값이 '0'이면 즉시 할인을 적용할 수 없습니다.
     */
    @JsonProperty("balance")
    private long balance;

    /**
     * 즉시 할인 코드(카드사 고유 번호)로 결제할 때 함께 넘겨야 하는 값입니다.
     */
    @JsonProperty("code")
    private String discountCode;

    /**
     * 할인 종료일입니다. yyyy-MM-dd 형식입니다. 종료일의 23:59:59까지 행사가 유효합니다.
     */
    @JsonProperty("dueDate")
    private LocalDate dueDate;

    /**
     * 즉시 할인을 적용할 수 있는 최소 결제 금액입니다.
     */
    @JsonProperty("minimumPaymentAmount")
    private long minimumPaymentAmount;

    /**
     * 즉시 할인을 적용할 수 있는 최대 결제 금액입니다.
     */
    @JsonProperty("maximumPaymentAmount")
    private long maximumPaymentAmount;
}
