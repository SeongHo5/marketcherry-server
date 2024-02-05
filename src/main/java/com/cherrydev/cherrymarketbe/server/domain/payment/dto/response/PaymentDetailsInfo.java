package com.cherrydev.cherrymarketbe.server.domain.payment.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailsInfo {

   @NotNull private Long accountId;
   @NotNull private Long orderId;
   @NotNull private String orderCode;
   @NotNull private TossPayment payment;
   @NotNull Integer totalAmount;
   Integer discount;
   Integer couponAmount;
   Integer rewordAmount;
   Integer deliveryFee;

}
