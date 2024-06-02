package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model;

import java.util.List;

import lombok.*;

import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TossPayment {

    @JsonProperty("version")
    private String version;

    @JsonProperty("paymentKey")
    private String paymentKey;

    private PaymentType type;

    @JsonProperty("orderCode")
    private String orderId;

    @JsonProperty("orderName")
    private String orderName;

    @JsonProperty("mId")
    private String mId;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("paymentMethods")
    private String method;

    @JsonProperty("totalAmount")
    private long totalAmount;

    @JsonProperty("balanceAmount")
    private long balanceAmount;

    private PaymentStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonProperty("requestedAt")
    private String requestedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonProperty("approvedAt")
    private String approvedAt;

    @JsonProperty("useEscrow")
    private boolean useEscrow;

    @JsonProperty("lastTransactionKey")
    private String lastTransactionKey;

    @JsonProperty("suppliedAmount")
    private long suppliedAmount;

    @JsonProperty("vat")
    private long vat;

    @JsonProperty("cultureExpense")
    private boolean cultureExpense;

    @JsonProperty("taxFreeAmount")
    private long taxFreeAmount;

    @JsonProperty("taxExemptionAmount")
    private long taxExemptionAmount;

    private List<Cancel> cancels;

    @JsonProperty("isPartialCancelable")
    private boolean isPartialCancelable;

    private Card card;

    private VirtualAccount virtualAccount;

    @JsonProperty("secret")
    private String secret;

    private MobilePhone mobilePhone;

    private GiftCertificate giftCertificate;

    private Transfer transfer;

    private Receipt receipt;

    private Checkout checkout;

    private EasyPay easyPay;

    @JsonProperty("country")
    private String country;

    private Failure failure;

    private CashReceipt cashReceipt;

    private TossDiscount tossDiscount;

}
