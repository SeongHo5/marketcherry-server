package com.cherrydev.cherrymarketbe.server.application.account.controller;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountQueryService;
import com.cherrydev.cherrymarketbe.server.application.account.service.AccountService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestModifyAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountQueryService accountQueryService;


    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final RequestSignUp requestSignUp) {
        accountService.createAccount(requestSignUp);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/me")
    @Cacheable(cacheNames = "accountCache", key = "#accountDetails.email", condition = "#accountDetails != null")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SELLER', 'ROLE_ADMIN')")
    public ResponseEntity<AccountInfo> getAccountInfo(
            @AuthenticationPrincipal final AccountDetails accountDetails
    ) {
        AccountInfo accountInfo = accountService.getAccountInfo(accountDetails);
        return ResponseEntity.ok(accountInfo);
    }

    /**
     * 내 정보 수정
     */
    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SELLER', 'ROLE_ADMIN')")
    public ResponseEntity<AccountInfo> modifyAccount(
            @AuthenticationPrincipal final AccountDetails accountDetails,
            @RequestBody final RequestModifyAccountInfo requestDto
    ) {
        AccountInfo accountInfo = accountService.resetPassword(accountDetails, requestDto);
        return ResponseEntity.ok(accountInfo);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SELLER')")
    public ResponseEntity<Void> dropOut(
            @AuthenticationPrincipal final AccountDetails accountDetails
    ) {
        accountService.deleteAccount(accountDetails);
        return ResponseEntity.ok().build();
    }

    /**
     * 이메일 중복 확인
     */
    @GetMapping("/email-check")
    public ResponseEntity<Void> checkDuplicateEmail(@RequestParam @Email final String email) {
        accountQueryService.existByEmail(email);
        return ResponseEntity.ok().build();
    }

}
