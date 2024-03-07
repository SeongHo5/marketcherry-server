package com.cherrydev.cherrymarketbe.server.application.auth.service;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountQueryService;
import com.cherrydev.cherrymarketbe.server.application.account.service.AccountService;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.AuthException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthTokenResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.REFRESH_TOKEN_EXPIRE_TIME;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType.LOCAL;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole.ROLE_CUSTOMER;

@Service
@RequiredArgsConstructor
public class CommonOAuthService {

    private final AccountQueryService accountQueryService;
    private final AccountService accountService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public SignInResponse processSignIn(
            final OAuthAccountInfo accountInfo,
            final String provider
    ) {
        String email = accountInfo.getEmail();
        String userName = accountInfo.getName();

        checkAndProcessOAuthRegistration(accountInfo, provider);
        JwtResponse jwtResponse = issueJwtToken(email);

        return createSignInResponse(jwtResponse, userName);
    }

    /**
     * 소셜 로그인 응답을 생성한다.
     * <p>
     * 소셜 로그인은 고객만 가능하므로, 응답 DTO의 userRole은 ROLE_CUSTOMER로 고정
     */
    private SignInResponse createSignInResponse(
            final JwtResponse jwtResponse,
            final String userName
    ) {
        return SignInResponse.builder()
                .userName(userName)
                .userRole(ROLE_CUSTOMER)
                .grantType(jwtResponse.grantType())
                .accessToken(jwtResponse.accessToken())
                .refreshToken(jwtResponse.refreshToken())
                .expiresIn(jwtResponse.accessTokenExpiresIn())
                .build();
    }

    /**
     * 사용자가 이미 가입되어 있는지 확인하고, 가입되어 있지 않다면 가입한다.
     */
    private void checkAndProcessOAuthRegistration(final OAuthAccountInfo accountInfo, final String provider) {
        String email = accountInfo.getEmail();
        RegisterType type = accountQueryService.getRegisterTypeByEmail(email);

        checkOAuthAccountType(type, provider);

        if (!accountQueryService.existByEmail(email)) {
            accountService.createAccountByOAuth(accountInfo, provider);
        }
    }

    /**
     * OAuth 인증이 완료된 사용자에게 토큰을 발급한다.
     */
    private JwtResponse issueJwtToken(final String email) {
        JwtResponse jwtResponse = jwtProvider.createJwtToken(email);
        redisService.setDataExpire(email, jwtResponse.refreshToken(), REFRESH_TOKEN_EXPIRE_TIME);
        return jwtResponse;
    }

    /**
     * OAuth 응답에 토큰이 정상 반환되었는지 확인하고, 있다면 토큰을 반환한다.
     * @param tokenResponse OAuth Response
     * @return AcessToken
     */
    public String getAcessTokenIfExist(final OAuthTokenResponse tokenResponse) {
        String accessToken = tokenResponse.getAccessToken();
        if (accessToken == null) {
            throw new AuthException(FAILED_HTTP_ACTION);
        }
        return accessToken;
    }

    private void checkOAuthAccountType(RegisterType type, String provider) {
        if (type != null && type.equals(LOCAL)){
            throw new DuplicatedException(LOCAL_ACCOUNT_ALREADY_EXIST);
        }
        if (type != null && !type.equals(RegisterType.valueOf(provider))) {
            throw new ServiceFailedException(INVALID_OAUTH_TYPE);
        }
    }
}
