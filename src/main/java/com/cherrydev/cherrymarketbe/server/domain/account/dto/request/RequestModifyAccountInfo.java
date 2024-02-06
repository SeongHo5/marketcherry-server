package com.cherrydev.cherrymarketbe.server.domain.account.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
@Builder
public record RequestModifyAccountInfo(
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,20}$",
                message = "비밀번호는 8자에서 20자 사이이며, 특수문자를 포함해야 합니다.")
        String password,
        String contact,
        @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12]\\d|3[01])$", message = "생년월일 형식이 일치하지 않습니다.")
        String birthdate
) {

}
