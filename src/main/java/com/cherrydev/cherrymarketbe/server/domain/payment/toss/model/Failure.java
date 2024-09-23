package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model;

/**
 * 토스 페이먼츠 PG 요청 실패 응답
 *
 * @param code 실패 코드
 * @param message 실패 메시지
 */
public record Failure(String code, String message) {}
