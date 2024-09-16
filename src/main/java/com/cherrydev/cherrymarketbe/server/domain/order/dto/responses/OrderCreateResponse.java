package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

public record OrderCreateResponse(String orderId, String orderName, Long amount) {}
