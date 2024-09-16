package com.cherrydev.cherrymarketbe.server.domain.auth.dto.response;

import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.BEARER_PREFIX;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtResponse;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SignInResponse {

  String userName;
  UserRole userRole;
  String grantType;
  String accessToken;
  String refreshToken;
  Long expiresIn;

  public static SignInResponse of(Account account, JwtResponse jwtResponse) {
    return SignInResponse.builder()
        .userName(account.getName())
        .userRole(account.getUserRole())
        .grantType(BEARER_PREFIX)
        .accessToken(jwtResponse.accessToken())
        .refreshToken(jwtResponse.refreshToken())
        .expiresIn(jwtResponse.accessTokenExpiresIn())
        .build();
  }
}
