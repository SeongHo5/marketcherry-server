package com.cherrydev.cherrymarketbe.server.application.common.utils;

import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.REFRESH_TOKEN_EXPIRE_TIME;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CookieUtil {

  public static ResponseCookie createCookie(String cookieName, String cookieValue) {

    return ResponseCookie.from(cookieName, cookieValue)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(REFRESH_TOKEN_EXPIRE_TIME)
        .build();
  }
}
