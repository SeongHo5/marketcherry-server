package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

public record GoodsSearchConditions(
    String goodsName, Long categoryId, Long makerId, Boolean isOnDiscount, String sort) {}
