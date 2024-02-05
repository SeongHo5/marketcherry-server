package com.cherrydev.cherrymarketbe.server.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public record RequestJwt(String accessToken, String refreshToken) {

}
