package com.cherrydev.cherrymarketbe.server.application.account.scheduler;

import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountTaskScheduler {

  private final AccountRepository accountRepository;

  @Transactional
  @Scheduled(cron = "0 0 1 * * ?")
  @Retryable(retryFor = {GenericJDBCException.class})
  public void releaseRestrictedAccounts() {
    log.info("===== 정지 계정 해제 시작 =====");
    accountRepository.releaseRestrictedAccount();
    log.info("===== 정지 계정 해제 종료 =====");
  }

  @Transactional
  @Scheduled(cron = "0 0 2 * * ?")
  @Retryable(retryFor = {GenericJDBCException.class})
  public void deleteInactiveAccounts() {
    log.info("===== 보관 계정 삭제 시작 =====");
    accountRepository.deleteInactiveAccount();
    log.info("===== 보관 계정 삭제 종료 =====");
  }
}
