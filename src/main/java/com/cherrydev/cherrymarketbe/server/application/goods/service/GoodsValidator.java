package com.cherrydev.cherrymarketbe.server.application.goods.service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.GOODS_STATUS_MISMATCHED;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.INSUFFICIENT_STOCK;
import static com.cherrydev.cherrymarketbe.server.domain.goods.enums.SalesStatus.ON_SALE;

import com.cherrydev.cherrymarketbe.server.application.exception.GoodsNotAvailableException;
import com.cherrydev.cherrymarketbe.server.application.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.domain.goods.enums.SalesStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoodsValidator {

  public void validateGoodsBeforeAddToCart(Goods goods) {
    validateGoodsSalesStatus(goods, ON_SALE);
    verifyGoodsInventory(goods);
  }

  protected void validateGoodsSalesStatus(Goods goods, SalesStatus neededSalesStatus) {
    if (goods.getSalesStatus() != neededSalesStatus) {
      throw new GoodsNotAvailableException(GOODS_STATUS_MISMATCHED);
    }
  }

  protected void verifyGoodsInventory(Goods goods) {
    if (goods.getInventory() <= 0) {
      throw new InsufficientStockException(INSUFFICIENT_STOCK, goods.getCode());
    }
  }
}
