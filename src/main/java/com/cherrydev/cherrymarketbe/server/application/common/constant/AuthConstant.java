package com.cherrydev.cherrymarketbe.server.application.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstant {

    // JWT Constants
    public static final String BLACKLISTED_KEY_PREFIX = "JWT::BLACK_LIST::";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofDays(2);
    public static final Duration REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(14);

    public static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";

    // OAuth Constants - NAVER
    public static final String OAUTH_NAVER_PREFIX = "OAUTH::NAVER::";
    public static final String OAUTH_NAVER_REFRESH_PREFIX = "OAUTH::NAVER::REFRESH::";

}
