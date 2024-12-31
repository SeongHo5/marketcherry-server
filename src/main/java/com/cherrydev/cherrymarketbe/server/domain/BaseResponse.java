package com.cherrydev.cherrymarketbe.server.domain;

/**
 * 표준 응답 객체
 *
 * @param message 응답 메시지(e.g., "OK", "CREATED")
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record BaseResponse<T>(String message, T data) {

  public static <T> BaseResponse<T> of(T data) {
    return new BaseResponse<>("OK", data);
  }
}
