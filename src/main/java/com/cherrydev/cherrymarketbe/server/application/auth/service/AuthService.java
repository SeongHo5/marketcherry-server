package com.cherrydev.cherrymarketbe.server.application.auth.service;

import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.*;
import static com.cherrydev.cherrymarketbe.server.application.common.service.template.EmailTemplate.*;
import static com.cherrydev.cherrymarketbe.server.application.common.service.template.EmailTemplate.createPasswordResetMessage;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator.generateRandomPassword;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.EMAIL_ALREADY_SENT;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.EMAIL_ALREADY_VERIFIED;
import static org.springframework.beans.propertyeditors.CustomBooleanEditor.VALUE_TRUE;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountQueryService;
import com.cherrydev.cherrymarketbe.server.application.auth.event.PasswordResetEvent;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.service.EmailService;
import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator;
import com.cherrydev.cherrymarketbe.server.application.exception.AuthException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
  private static final int NEW_PASSWORD_LENGTH = 12;

  private final JwtProvider jwtProvider;
  private final RedisService redisService;
  private final EmailService emailService;
  private final AccountQueryService accountQueryService;
  private final AuthValidator authValidator;
  private final PasswordEncoder passwordEncoder;
  private final ApplicationEventPublisher eventPublisher;

  /**
   * 로그인 처리를 위해 사용자를 조회하고, 비밀번호를 검증한다.
   *
   * @param requestSignIn 로그인 요청 정보(E-mail, PW)
   * @return 로그인 응답 정보(토큰, 토큰 유효기간, 사용자 이름, 사용자 권한)
   */
  @Transactional
  public SignInResponse signIn(final RequestSignIn requestSignIn) {
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

  /**
   * 이미 발송된 코드가 있는지 확인하고, 없으면 인증 코드 생성 후 이메일 전송
   *
   * <p><em>사용시 주의 사항</em>
   *
   * <p>※ 이메일 발송에 약 7초 정도 소요됩니다.
   *
   * @param email 전송할 이메일
   */
  public void sendVerificationMail(final String email) {
    checkIfEmailIsWhiteListed(email);
    checkIfCodeAlreadySent(email, PREFIX_VERIFY);

    String verificationCode = CodeGenerator.generateRandomCode(VERIFICATION_CODE_LENGTH);

    redisService.setDataExpire(
        PREFIX_VERIFY + email, verificationCode, VERIFICATION_CODE_EXPIRE_TIME);
    emailService.sendMail(email, VERIFICATION_TITTLE, createVerificationMessage(verificationCode));
  }

  public void verifyEmailByCode(final String email, final String code) {
    authValidator.verifyEmail(email, code);
  }

  public void sendPasswordResetMail(final String email) {
    checkIfCodeAlreadySent(email, PREFIX_PW_RESET);

    String verificationCode = CodeGenerator.generateRandomCode(VERIFICATION_CODE_LENGTH);

    redisService.setDataExpire(
        PREFIX_PW_RESET + email, verificationCode, VERIFICATION_CODE_EXPIRE_TIME);
    emailService.sendMail(email, PW_RESET_TITTLE, createPasswordResetMessage(verificationCode));
  }

  /**
   * 비밀번호 재설정 코드 검증 & 비밀번호 재설정
   *
   * @param email 이메일
   * @param verificationCode 인증 코드
   * @return 재설정된 비밀번호
   */
  @Transactional
  public String verifyPasswordResetEmail(final String email, final String verificationCode) {
    authValidator.verifyResetCode(email, verificationCode);

    String newPassword = generateRandomPassword(NEW_PASSWORD_LENGTH);
    String encodedPassword = passwordEncoder.encode(newPassword);

    Account account = accountQueryService.fetchAccountEntity(email);
    account.updatePassword(encodedPassword);
    publishPasswordResetEvent(account);
    return newPassword;
  }

  // =============== PRIVATE METHODS =============== //

  /** REDIS에서 사용자의 정보를 삭제하고, 토큰을 BLACK_LIST에 추가해 토큰을 무효화한다. */
  private void invalidateToken(String email, String accessToken) {
    redisService.deleteData(email);
    redisService.setDataExpire(
        BLACKLISTED_KEY_PREFIX + accessToken, VALUE_TRUE, REFRESH_TOKEN_EXPIRE_TIME);
  }

  private void checkIfCodeAlreadySent(final String email, final String prefix) {
    if (redisService.hasKey(prefix + email)) {
      throw new AuthException(EMAIL_ALREADY_SENT);
    }
  }

  private void checkIfEmailIsWhiteListed(final String email) {
    if (redisService.hasKey(PREFIX_VERIFIED + email)) {
      throw new AuthException(EMAIL_ALREADY_VERIFIED);
    }
  }

  private void publishPasswordResetEvent(Account account) {
    PasswordResetEvent event = new PasswordResetEvent(this, account.getEmail());
    eventPublisher.publishEvent(event);
  }
}
