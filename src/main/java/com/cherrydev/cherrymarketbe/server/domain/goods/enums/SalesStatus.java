package com.cherrydev.cherrymarketbe.server.domain.goods.enums;

public enum SalesStatus {
  ON_SALE,
  PAUSE,
  DISCONTINUANCE;

  public boolean isOnSale() {
    return this == ON_SALE;
  }
}
