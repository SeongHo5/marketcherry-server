package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobilePhone {

    @JsonProperty("customerMobilePhone")
    private String customerMobilePhone;

    @JsonProperty("settlementStatus")
    private SettlementStatus settlementStatus;

    @JsonProperty("receiptUrl")
    private String receiptUrl;

    // Ensure that the SettlementStatus class or enum is also defined in your Java codebase
}
