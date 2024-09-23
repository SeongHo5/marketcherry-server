package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestFreeCard {
  /**
   * 카드사 숫자 코드입니다. [카드사
   * 코드](https://docs.tosspayments.com/reference/codes#%EC%B9%B4%EB%93%9C%EC%82%AC-%EC%BD%94%EB%93%9C)를
   * 참고하세요.
   */
  @JsonProperty("companyCode")
  private String companyCode;

  /** 무이자 할부 행사 종료일입니다. yyyy-MM-dd 형식입니다. 종료일의 23:59:59까지 행사가 유효합니다. */
  @JsonProperty("dueDate")
  private LocalDate dueDate;

  /** 무이자 할부를 적용할 수 있는 개월 수 입니다. */
  @JsonProperty("installmentFreeMonths")
  private List<Long> installmentFreeMonths;

  /** 무이자 할부를 적용할 수 있는 최소 결제 금액입니다. */
  @JsonProperty("minimumPaymentAmount")
  private long minimumPaymentAmount;
}
