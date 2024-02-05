package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cancel {

    @JsonProperty("cancelAmount")
    private Long cancelAmount;

    @JsonProperty("cancelReason")
    private String cancelReason;

    @JsonProperty("taxFreeAmount")
    private Long taxFreeAmount;

    @JsonProperty("taxExemptionAmount")
    private Long taxExemptionAmount;

    @JsonProperty("refundableAmount")
    private Long refundableAmount;

    @JsonProperty("easyPayDiscountAmount")
    private Long easyPayDiscountAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonProperty("canceledAt")
    private OffsetDateTime canceledAt;

    @JsonProperty("transactionKey")
    private String transactionKey;
}
