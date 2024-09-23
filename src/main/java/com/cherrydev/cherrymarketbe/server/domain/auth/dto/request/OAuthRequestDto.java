package com.cherrydev.cherrymarketbe.server.domain.auth.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class OAuthRequestDto {

  @NotNull String authCode;

  @Nullable String state;

  @Pattern(regexp = "NAVER|KAKAO|GOOGLE", message = "지원하지 않는 OAuth 제공자입니다.")
  String provider;
}
