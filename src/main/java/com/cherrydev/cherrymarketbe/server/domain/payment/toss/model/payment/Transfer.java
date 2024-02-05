package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @JsonProperty("bankCode")
    private String bankCode;

    @JsonProperty("settlementStatus")
    private SettlementStatus settlementStatus;

}
