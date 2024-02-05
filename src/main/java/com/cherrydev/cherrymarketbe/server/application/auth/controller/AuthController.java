package com.cherrydev.cherrymarketbe.server.application.auth.controller;

import com.cherrydev.cherrymarketbe.server.application.auth.service.AuthService;
import com.cherrydev.cherrymarketbe.server.application.common.service.EmailService;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtReissueResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.RequestJwt;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> signIn(
            @RequestBody @Valid final RequestSignIn requestSignIn
    ) {
        SignInResponse response = authService.signIn(requestSignIn);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, BEARER_PREFIX + response.getAccessToken())
                .body(response);
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/{access_token}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> signOut(
            @PathVariable("access_token") final String accessToken
    ) {
        authService.signOut(accessToken);
        return ResponseEntity.ok().build();
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/re-issue")
    public ResponseEntity<JwtReissueResponse> reissue(
            @RequestBody final RequestJwt requestJwt
    ) {
        JwtReissueResponse response = authService.reissue(requestJwt);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, response.accessToken())
                .body(response);
    }

    /**
     * 본인 인증 이메일 발송
     *
     * @param email 메일 보낼 주소
     */
    @PostMapping("/email-verification/send-mail")
    public ResponseEntity<Void> sendEmail(
            @RequestParam @Email final String email
    ) {
        emailService.sendVerificationMail(email);
        return ResponseEntity.ok().build();
    }

    /**
     * 본인 인증 확인
     *
     * @param verificationCode 인증 코드
     */
    @GetMapping("/email-verification/verify")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam final String email,
            @RequestParam final String verificationCode
    ) {
        authService.verifyEmailByCode(email, verificationCode);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 재설정 이메일 발송
     *
     * @param email 메일 보낼 주소
     */
    @PostMapping("/pw-reset/send-mail")
    public ResponseEntity<Void> sendPasswordResetEmail(
            @RequestParam @Email final String email
    ) {
        emailService.sendPasswordResetMail(email);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 재설정 확인
     *
     * @param verificationCode 인증 코드
     */
    @GetMapping("/pw-reset/verify")
    public ResponseEntity<String> verifyPasswordResetEmail(
            @RequestParam final String email,
            @RequestParam final String verificationCode
    ) {
        String response = authService.verifyPasswordResetEmail(email, verificationCode);
        return ResponseEntity.ok(response);
    }

}
