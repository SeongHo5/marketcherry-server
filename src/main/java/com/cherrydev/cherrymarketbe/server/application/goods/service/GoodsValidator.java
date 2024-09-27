package com.cherrydev.cherrymarketbe.server.application.goods.service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.GOODS_STATUS_MISMATCHED;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.INSUFFICIENT_STOCK;

import com.cherrydev.cherrymarketbe.server.application.exception.GoodsNotAvailableException;
import com.cherrydev.cherrymarketbe.server.application.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoodsValidator {

  public void validateGoodsBeforeAddToCart(Goods goods) {
    this.validateGoodsSalesStatus(goods);
    this.verifyGoodsInventory(goods);
  }

  protected void validateGoodsSalesStatus(Goods goods) {
    if (!goods.isOnSale()) {
      throw new GoodsNotAvailableException(GOODS_STATUS_MISMATCHED);
    }
  }

  protected void verifyGoodsInventory(Goods goods) {
    if (goods.isOutOfStock()) {
      throw new InsufficientStockException(INSUFFICIENT_STOCK, goods.getCode());
    }
  }
}
