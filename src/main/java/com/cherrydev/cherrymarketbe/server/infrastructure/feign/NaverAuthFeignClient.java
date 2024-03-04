package com.cherrydev.cherrymarketbe.server.infrastructure.feign;

import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverOauthClient" , url = "https://nid.naver.com/oauth2.0")
public interface NaverAuthFeignClient {

    @PostMapping("/token")
    OAuthTokenResponse getOAuthToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    );





}
