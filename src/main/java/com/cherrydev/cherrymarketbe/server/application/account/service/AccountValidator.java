package com.cherrydev.cherrymarketbe.server.application.account.service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.CONFLICT_ACCOUNT;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.PROHIBITED_USERNAME;

import com.cherrydev.cherrymarketbe.server.application.exception.AuthException;
import com.cherrydev.cherrymarketbe.server.application.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.ForbiddenUserName;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AccountRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {

  private final AccountRepository accountRepository;

  /** 회원 가입 시 이메일이 중복되는지 확인한다. */
  protected void checkEmailIsDuplicated(String email) {
    if (accountRepository.existsByEmail(email)) {
      throw new DuplicatedException(CONFLICT_ACCOUNT);
    }
  }

  /**
   * 회원 가입 시 이름에 금지어가 포함되어 있는지 확인한다.
   *
   * @see ForbiddenUserName 금지어 목록
   */
  protected void checkUsernameIsProhibited(String username) {
    boolean isProhibited =
        Arrays.stream(ForbiddenUserName.values())
            .anyMatch(forbiddenUserName -> forbiddenUserName.isForbidden(username));

    if (isProhibited) {
      throw new AuthException(PROHIBITED_USERNAME);
    }
  }
}
