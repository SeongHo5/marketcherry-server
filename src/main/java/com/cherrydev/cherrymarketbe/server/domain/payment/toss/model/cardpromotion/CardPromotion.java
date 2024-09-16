package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.cardpromotion;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPromotion {
  /** 카드사의 즉시 할인 정보입니다. */
  @JsonProperty("discountCards")
  private List<DiscountCard> discountCards = Collections.emptyList();

  /** 카드사의 무이자 할부 정보입니다. */
  @JsonProperty("interestFreeCards")
  private List<InterestFreeCard> interestFreeCards = Collections.emptyList();
}
