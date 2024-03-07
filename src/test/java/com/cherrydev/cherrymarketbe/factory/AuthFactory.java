package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.RequestJwt;

public class AuthFactory {

    public static RequestSignIn createSignInRequestDtoA() {
        return new RequestSignIn("boram17@example.org", "Password12#");
    }

    public static RequestSignIn createSignInRequestDtoB() {
        return new RequestSignIn("boram17@example.org", "Password1212");
    }

    public static RequestSignIn createSignInRequestDtoC() {
        return new RequestSignIn("hellonon1@example.org", "Password12#");
    }

    public static RequestSignIn createSignInRequestDtoD() {
        return new RequestSignIn("test1@marketcherry.com", "Password12#");
    }

    public static RequestSignIn createSignInRequestDtoE() {
        return new RequestSignIn("test2@marketcherry.com", "Password12#");
    }

    public static RequestSignIn createSignInRequestDtoF() {
        return new RequestSignIn("noyeongjin@example.org", "Password12#");
    }

    public static RequestJwt createJwtRequestDto(JwtResponse jwtResponse) {
        return new RequestJwt(jwtResponse.accessToken(), jwtResponse.refreshToken());
    }

    public static RequestJwt createJwtRequestDto(String accessToken, String refreshToken) {
        return new RequestJwt(accessToken, refreshToken);
    }

}
