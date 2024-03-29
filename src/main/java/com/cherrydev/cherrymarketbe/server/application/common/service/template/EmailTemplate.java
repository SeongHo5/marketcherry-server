package com.cherrydev.cherrymarketbe.server.application.common.service.template;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static j2html.TagCreator.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailTemplate {

    public static final String VERIFICATION_TITTLE = "[CherryMarket] 회원가입 본인 인증 메일입니다.";

    public static String createVerificationMessage(final String verificationCode) {
        return html(
                head(
                        title("회원가입 본인 인증 메일입니다.")
                ),
                body(
                        div(
                                h1("회원가입 본인 인증 메일입니다."),
                                p("서비스를 안전하게 즐기기 위해 홈페이지에서 아래의 인증 코드를 입력해주세요."),
                                h1(verificationCode).withStyle("font-size: 30px; font-weight: bold; margin-bottom: 20px; margin-top: 20px;"),
                                p("- 인증 코드는 3분간 유효하며, 유효 시간이 지난 경우 다시 인증해주세요."),
                                p("- 타인이 이메일을 잘못 입력한 경우, 본 메일이 발송될 수 있습니다.")
                        ).withStyle("font-family: 'Noto Sans KR', sans-serif; width: 100%; height: 100%; text-align: center; color: #333;")
                )
        ).render();
    }

    public static final String PW_RESET_TITTLE = "[CherryMarket] 비밀번호 재설정 메일입니다.";

    public static String createPasswordResetMessage(final String verificationCode) {
        return html(
                head(
                        title("비밀번호 재설정 메일입니다.")
                ),
                body(
                        div(
                                h1("비밀번호 재설정 메일입니다."),
                                p("아래의 인증 코드를 입력해주세요."),
                                h1(verificationCode).withStyle("font-size: 30px; font-weight: bold; margin-bottom: 20px; margin-top: 20px;"),
                                p("본인이 요청한 것이 아니라면, 고객센터에 문의해주세요."),
                                p("감사합니다.")
                        ).withStyle("font-family: 'Noto Sans KR', sans-serif; width: 100%; height: 100%; text-align: center; color: #333;")
                )
        ).render();
    }

    public static final String PW_RESET_INFO_TITTLE = "[CherryMarket] 비밀번호가 재설정되었습니다.";

    public static String createPasswordResetInfoMessage() {
        return html(
                head(
                        title("비밀번호가 성공적으로 재설정되었습니다.")
                ),
                body(
                        div(
                                h1("비밀번호가 재설정되었습니다."),
                                p("본인이 재설정하지 않았다면, 고객센터에 문의해주세요."),
                                p("감사합니다.")
                        ).withStyle("font-family: 'Noto Sans KR', sans-serif; width: 100%; height: 100%; text-align: center; color: #333;")
                )
        ).render();
    }

}
