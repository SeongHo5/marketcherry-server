package com.cherrydev.cherrymarketbe.server.application.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Auditing을 위한 현재 사용자 정보를 제공하는 클래스입니다.<br>
 * Spring Security의 {@link SecurityContextHolder}를 사용하여 현재 사용자 정보를 가져옵니다.<br>
 * 인증 정보가 없거나, 인증되지 않은 경우 Optional.empty()를 반환합니다.<br>
 * 현재 컨텍스트에서 인증 정보를 가져오므로, 컨텍스트가 전파되지 않는 작업(e.g. @Async)에서는 사용이 불가능할 수 있습니다.
 */
@Component
public class CustomAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isNotAuthenticated = authentication == null || !authentication.isAuthenticated();
    if (isNotAuthenticated) {
      return Optional.empty();
    }
    return Optional.ofNullable(authentication.getName());
  }
}
