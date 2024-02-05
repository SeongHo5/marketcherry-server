package com.cherrydev.cherrymarketbe.server.application.common.constant;

import java.time.Duration;

/**
 * 이메일 기능 관련 상수
 */
public final class EmailConstant {

    public static final String PREFIX_VERIFY = "EMAIL::VERIFY::";
    public static final String PREFIX_VERIFIED = "EMAIL::VERIFIED::";
    public static final String PREFIX_PW_RESET = "EMAIL::PW_RESET::";
    public static final String VALUE_TRUE = "TRUE";

    public static final String ENCODING_CHARSET = "UTF-8";
    public static final String MAILER_SUBTYPE = "HTML";

    public static final int VERIFICATION_CODE_LENGTH = 6;
    public static final Duration VERIFICATION_CODE_EXPIRE_TIME = Duration.ofMinutes(3);
    public static final Duration WHITE_LIST_VERIFIED_TIME = Duration.ofDays(1);

    private EmailConstant() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

}
