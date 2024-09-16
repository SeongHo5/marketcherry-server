package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

/**
 * 결제창이 열리는 주소입니다.
 *
 * @param url 결제창이 열리는 주소
 */
public record Checkout(String url) {}
