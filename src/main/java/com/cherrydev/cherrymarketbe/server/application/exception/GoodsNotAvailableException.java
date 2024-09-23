package com.cherrydev.cherrymarketbe.server.application.exception;

import lombok.Getter;

@Getter
public class GoodsNotAvailableException extends ApplicationException {

  public GoodsNotAvailableException(ExceptionStatus status) {
    super(status);
  }
}
