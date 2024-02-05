package com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth;

import com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.naver.NaverAccountResponse;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INVALID_INPUT_VALUE;


@Value
@NoArgsConstructor(force = true)
public class OAuthAccountInfo {

    @NotNull
    String id;

    @NotNull
    String name;

    @NotNull
    String email;

    @Nullable
    String contact;

    public OAuthAccountInfo(NaverAccountResponse naverAccount) {
        NaverAccountResponse.NaverAccount naverAccountResponse = naverAccount.getNaverAccount();
        this.id = naverAccountResponse.getId();
        this.name = naverAccountResponse.getName();
        this.email = naverAccountResponse.getEmail();
        this.contact = naverAccountResponse.getContact();
    }

}
