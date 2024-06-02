package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CashReceipt {
    @JsonProperty("receiptKey")
    private String receiptKey;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("taxFreeAmount")
    private Long taxFreeAmount;

    @JsonProperty("issueNumber")
    private String issueNumber;

    @JsonProperty("receiptUrl")
    private String receiptUrl;
}
