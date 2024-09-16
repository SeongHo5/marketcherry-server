package com.cherrydev.cherrymarketbe.server.infrastructure.feign;

import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverUserClient", url = "https://openapi.naver.com/")
public interface NaverUserFeignClient {

  @GetMapping("/v1/nid/me")
  @Headers("Authorization: {accessToken}")
  OAuthAccountInfo getAccountInfo(@RequestHeader("Authorization") String accessToken);
}
