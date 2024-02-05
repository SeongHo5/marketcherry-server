package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

public record SearchCondition(String goodsName, Long categoryId, Long makerId, String goodsCode, String salesStatus) {
}
