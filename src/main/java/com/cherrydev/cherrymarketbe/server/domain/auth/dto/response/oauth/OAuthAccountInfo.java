package com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import lombok.Value;

import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.naver.NaverAccountResponse;


@Value
@NoArgsConstructor(force = true)
public class OAuthAccountInfo {

    @NotNull String id;

    @NotNull String name;

    @NotNull String email;

    @Nullable String contact;

    public OAuthAccountInfo(NaverAccountResponse naverAccount) {
        NaverAccountResponse.NaverAccount naverAccountResponse = naverAccount.getNaverAccount();
        this.id = naverAccountResponse.getId();
        this.name = naverAccountResponse.getName();
        this.email = naverAccountResponse.getEmail();
        this.contact = naverAccountResponse.getContact();
    }

}
