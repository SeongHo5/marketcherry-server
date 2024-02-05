package com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelForm {

    @NotNull
    private String cancelReason;

    private Number cancelAmount ;

    private String currency;

    private Boolean dividedPayment;

    private RefundReceiveAccount refundReceiveAccount;

    private Number taxAmount;

    private Integer taxExemptionAmount ;

    private Number taxFreeAmount ;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefundReceiveAccount {
        private String bank;
        private String accountNumber;
        private String holderName;
    }

    public Map<String, Object> toRequestBody() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cancelReason", this.getCancelReason());
        requestBody.put("cancelAmount", this.getCancelAmount());
        requestBody.put("currency ", this.getCurrency());
        requestBody.put("accountNumber  ", this.refundReceiveAccount.getAccountNumber());
        requestBody.put("bank ", this.refundReceiveAccount.getBank());
        requestBody.put("holderName ", this.refundReceiveAccount.getHolderName());
        requestBody.put("taxAmount ", this.getTaxAmount());
        requestBody.put("taxExemptionAmount  ", this.getTaxExemptionAmount());
        requestBody.put("taxFreeAmount  ", this.getTaxFreeAmount());

        return requestBody;
    }
}
