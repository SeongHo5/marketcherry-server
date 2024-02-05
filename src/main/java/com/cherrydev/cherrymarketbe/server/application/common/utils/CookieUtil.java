package com.cherrydev.cherrymarketbe.server.application.common.utils;

import com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant;
import org.springframework.http.ResponseCookie;

public final class CookieUtil {

    private CookieUtil() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static ResponseCookie createCookie(String cookieName, String cookieValue) {

        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(AuthConstant.REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

}
