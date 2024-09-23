package com.cherrydev.cherrymarketbe.server.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestSignIn(
    @Email(message = "유효한 이메일 주소를 입력하세요.") String email,
    @Size(min = 8, max = 20, message = "비밀번호는 8 ~ 20자 사이여야 합니다.") @NotNull String password) {}
