package com.cherrydev.cherrymarketbe.server.application.auth.service;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountQueryService;
import com.cherrydev.cherrymarketbe.server.application.auth.event.PasswordResetEvent;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtReissueResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.RequestJwt;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant.BLACK_LIST_KEY_PREFIX;
import static com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant.REFRESH_TOKEN_EXPIRE_TIME;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator.generateRandomPassword;
import static org.springframework.beans.propertyeditors.CustomBooleanEditor.VALUE_TRUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final AccountQueryService accountQueryService;
    private final AuthValidator authValidator;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    private static final int NEW_PASSWORD_LENGTH = 12;

    /**
     * 로그인 처리를 위해 사용자를 조회하고, 비밀번호를 검증한다.
     *
     * @param requestSignIn 로그인 요청 정보(E-mail, PW)
     * @return 로그인 응답 정보(토큰, 토큰 유효기간, 사용자 이름, 사용자 권한)
     */
    @Transactional
    public SignInResponse signIn(
            final RequestSignIn requestSignIn
    ) {
        String email = requestSignIn.email();
        Account account = accountQueryService.fetchAccountEntity(email);

        authValidator.checkUserStatusByEmail(account);
        authValidator.checkPasswordIsCorrect(requestSignIn.password(), account.getPassword());

        JwtResponse jwtResponse = jwtProvider.createJwtToken(email);
        redisService.setDataExpire(email, jwtResponse.refreshToken(), REFRESH_TOKEN_EXPIRE_TIME);
        return SignInResponse.of(account, jwtResponse);
    }

    /**
     * 로그아웃 처리를 위해 사용자 인증 수단(토큰)을 검증하고, 무효화한다.
     *
     * @param accessToken 인증 수단(토큰)
     */
    @Transactional
    public void signOut(final String accessToken) {
        jwtProvider.validateToken(accessToken);

        Claims claims = jwtProvider.getInfoFromToken(accessToken);
        String email = claims.getSubject();

        invalidateToken(email, accessToken);
    }

    /**
     * 토큰 재발급
     *
     * @param requestJwt 기존 토큰
     * @return 재발급된 토큰
     */
    @Transactional
    public JwtReissueResponse reissue(final RequestJwt requestJwt) {

        authValidator.validateRefreshToken(requestJwt);
        authValidator.validateRefreshTokenOwnership(requestJwt);

        String email = jwtProvider.getInfoFromToken(requestJwt.accessToken()).getSubject();

        JwtResponse jwtResponse = jwtProvider.createJwtToken(email);

        return JwtReissueResponse.builder()
                .accessToken(jwtResponse.accessToken())
                .refreshToken(jwtResponse.refreshToken())
                .accessTokenExpiresIn(jwtResponse.accessTokenExpiresIn())
                .build();
    }

    public void verifyEmailByCode(final String email, final String code) {
        authValidator.verifyEmail(email, code);
    }

    /**
     * 비밀번호 재설정 코드 검증 & 비밀번호 재설정
     *
     * @param email            이메일
     * @param verificationCode 인증 코드
     * @return 재설정된 비밀번호
     */
    @Transactional
    public String verifyPasswordResetEmail(
            final String email,
            final String verificationCode
    ) {
        authValidator.verifyResetCode(email, verificationCode);

        String newPassword = generateRandomPassword(NEW_PASSWORD_LENGTH);
        String encodedPassword = passwordEncoder.encode(newPassword);

        Account account = accountQueryService.fetchAccountEntity(email);
        account.updatePassword(encodedPassword);
        publishPasswordResetEvent(account);
        return newPassword;
    }

    // =============== PRIVATE METHODS =============== //


    /**
     * REDIS에서 사용자의 정보를 삭제하고, 토큰을 BLACK_LIST에 추가해 토큰을 무효화한다.
     */
    private void invalidateToken(String email, String accessToken) {
        redisService.deleteData(email);
        redisService.setDataExpire(
                BLACK_LIST_KEY_PREFIX + accessToken,
                VALUE_TRUE, REFRESH_TOKEN_EXPIRE_TIME);
    }

    private void publishPasswordResetEvent(Account account) {
        PasswordResetEvent event = new PasswordResetEvent(this, account.getEmail());
        eventPublisher.publishEvent(event);
    }
}
