package com.cherrydev.cherrymarketbe.server.application.auth.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.AuthException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.RequestJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.server.application.common.constant.EmailConstant.*;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus.DELETED;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus.RESTRICTED;
import static org.springframework.beans.propertyeditors.CustomBooleanEditor.VALUE_TRUE;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;


    /**
     * 계정의 상태를 검증한다.
     *
     * @param account 계정 정보
     * @throws NotFoundException 계정이 삭제(DELETED) 상태일 경우
     * @throws AuthException     계정이 제한(RESTRICTED) 상태일 경우
     * @see UserStatus
     */
    protected void checkUserStatusByEmail(Account account) {
        if (account.getUserStatus().equals(DELETED)) {
            throw new NotFoundException(DELETED_ACCOUNT);
        }
        if (account.getUserStatus().equals(RESTRICTED)) {
            throw new AuthException(RESTRICTED_ACCOUNT);
        }
    }

    /**
     * 요청된 비밀번호와 계정의 비밀번호가 일치하는지 검증한다.
     */
    protected void checkPasswordIsCorrect(String requestedPassword, String encodedPassword) {
        if (!passwordEncoder.matches(requestedPassword, encodedPassword)) {
            throw new AuthException(INVALID_ID_OR_PW);
        }
    }

    /**
     * 토큰의 유효성을 검증한다.
     */
    protected void validateRefreshToken(RequestJwt requestJwt) {
        jwtProvider.validateToken(requestJwt.refreshToken());
    }

    /**
     * 요청자와 토큰의 소유자 정보가 일치하는지 검증한다.
     */
    protected void validateRefreshTokenOwnership(RequestJwt requestJwt) {
        String email = jwtProvider.getInfoFromToken(requestJwt.accessToken()).getSubject();
        String validRefreshToken = redisService.getData(email, String.class);
        if (!requestJwt.refreshToken().equals(validRefreshToken)) {
            throw new AuthException(EXPIRED_REFRESH_TOKEN);
        }
    }

    /**
     * 본인 인증 코드 검증
     * <p>
     * 인증에 성공하면 1일간 인증된 이메일로 등록
     *
     * @param email 인증 코드를 보냈던 이메일
     * @param code  검증할(사용자가 입력한) 인증 코드
     */
    protected void verifyEmail(final String email, final String code) {
        if (!redisService.hasKey(PREFIX_VERIFY + email)) {
            throw new NotFoundException(NOT_FOUND_REDIS_KEY);
        }

        String validCode = redisService.getData(PREFIX_VERIFY + email, String.class);
        if (!code.equals(validCode)) {
            throw new AuthException(INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisService.setDataExpire(PREFIX_VERIFIED + email, VALUE_TRUE, WHITE_LIST_VERIFIED_TIME);
        redisService.deleteData(PREFIX_VERIFY + email);
    }

    /**
     * 비밀번호 재설정 코드 검증
     *
     * @return 검증 성공 여부
     */
    protected void verifyResetCode(final String email, final String code) {
        String validCode = redisService.getData(PREFIX_PW_RESET + email, String.class);
        if (!code.equals(validCode)) {
            throw new AuthException(INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisService.deleteData(PREFIX_PW_RESET + email);
    }

}
