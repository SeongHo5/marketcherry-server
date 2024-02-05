package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EasyPay {

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("discountAmount")
    private Long discountAmount;
}
