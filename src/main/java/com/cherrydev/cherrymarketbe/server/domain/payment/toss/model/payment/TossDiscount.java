package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TossDiscount {
    @JsonProperty("amount")
    private long amount;
}
