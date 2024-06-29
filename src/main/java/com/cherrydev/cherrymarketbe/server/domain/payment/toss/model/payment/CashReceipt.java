package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

public record CashReceipt(
        String receiptKey,
        String type,
        String amount,
        Long taxFreeAmount,
        String issueNumber,
        String receiptUrl
) {
}
