package com.cherrydev.cherrymarketbe.server.application.common.utils;

import org.springframework.http.ResponseCookie;

import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.REFRESH_TOKEN_EXPIRE_TIME;

public final class CookieUtil {

    private CookieUtil() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static ResponseCookie createCookie(String cookieName, String cookieValue) {

        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

}
