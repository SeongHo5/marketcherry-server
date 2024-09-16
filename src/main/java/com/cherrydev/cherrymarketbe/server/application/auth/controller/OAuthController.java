package com.cherrydev.cherrymarketbe.server.application.auth.controller;

import com.cherrydev.cherrymarketbe.server.application.auth.service.NaverOAuthService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.OAuthRequestDto;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

  private final NaverOAuthService naverService;

  @PostMapping("/naver/sign-in")
  public ResponseEntity<SignInResponse> naverSignIn(
      final @RequestBody OAuthRequestDto oAuthRequestDto) {
    return ResponseEntity.ok(naverService.signIn(oAuthRequestDto));
  }

  @DeleteMapping("/naver/sign-out")
  @PreAuthorize("hasRole('ROLE_CUSTOMER')")
  public ResponseEntity<Void> naverSignOut(
      final @AuthenticationPrincipal AccountDetails accountDetails) {
    return naverService.signOut(accountDetails);
  }
}
