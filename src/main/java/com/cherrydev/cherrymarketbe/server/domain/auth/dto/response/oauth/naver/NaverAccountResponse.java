package com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.naver;

import lombok.Value;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
public class NaverAccountResponse {

    @JsonProperty("resultcode")
    String resultCode;

    String message;

    @JsonProperty("response")
    NaverAccount naverAccount;

    @Value
    public static class NaverAccount {

        String id;

        String gender;

        String email;

        @JsonProperty("mobile")
        String contact;

        String name;


    }

}
