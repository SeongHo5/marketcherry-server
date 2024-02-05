package com.cherrydev.cherrymarketbe.server.domain.core.dto;


import lombok.Builder;

@Builder
public record JwtReissueResponse(String accessToken, String refreshToken, Long accessTokenExpiresIn) {

}
