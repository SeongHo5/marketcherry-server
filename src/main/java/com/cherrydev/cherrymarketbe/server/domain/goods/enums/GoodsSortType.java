package com.cherrydev.cherrymarketbe.server.domain.goods.enums;

public enum GoodsSortType {
  PRICE_ASC,
  PRICE_DESC,
  NEWEST,
  OLDEST;

  public boolean isEquals(String sort) {
    return this.name().equals(sort.toUpperCase());
  }
}
