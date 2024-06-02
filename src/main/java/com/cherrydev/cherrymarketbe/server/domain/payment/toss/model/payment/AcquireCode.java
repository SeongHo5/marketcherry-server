package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import lombok.Getter;

@Getter
public enum AcquireCode {

    IBK_BC("3K", "기업비씨"),
    GWANGJUBANK("46", "광주은행"),
    LOTTE("71", "롯데카드"),
    KDBBANK("30", "KDB산업은행"),
    BC("31", "BC카드"),
    SAMSUNG("51", "삼성카드"),
    SAEMAUL("38", "새마을금고"),
    SHINHAN("41", "신한카드"),
    SHINHYEOP("62", "신협"),
    CITI("36", "씨티카드"),
    WOORI_BC("33", "우리BC카드(BC 매입)"),
    WOORI("W1", "우리카드(우리 매입)"),
    POST("37", "우체국예금보험"),
    SAVINGBANK("39", "저축은행중앙회"),
    JEONBUKBANK("35", "전북은행"),
    JEJUBANK("42", "제주은행"),
    KAKAOBANK("15", "카카오뱅크"),
    KBANK("3A", "케이뱅크"),
    TOSSBANK("24", "토스뱅크"),
    HANA("21", "하나카드"),
    HYUNDAI("61", "현대카드"),
    KOOKMIN("11", "KB국민카드"),
    NONGHYEOP("91", "NH농협카드"),
    SUHYEOP("34", "Sh수협은행");

    private final String code;
    private final String description;

    AcquireCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByCode(String code) {
        for (AcquireCode ac : AcquireCode.values()) {
            if (ac.getCode().equals(code)) {
                return ac.getDescription();
            }
        }
        return "Unknown code";
    }
}
