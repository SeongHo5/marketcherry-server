package com.cherrydev.cherrymarketbe.server.application.account.service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.NOT_FOUND_ACCOUNT;

import com.cherrydev.cherrymarketbe.server.application.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.AccountSearchConditions;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AccountRepository;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.CustomAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

  private final AccountRepository accountRepository;
  private final CustomAccountRepository customAccountRepository;

  public Account fetchAccountEntity(final String email) {
    return accountRepository
        .findByEmail(email)
        .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
  }

  public Page<Account> findByConditions(Pageable pageable, AccountSearchConditions conditions) {
    return customAccountRepository.findByConditions(pageable, conditions);
  }

  public RegisterType getRegisterTypeByEmail(final String email) {
    return accountRepository.getRegisterTypeByEmail(email);
  }

  public boolean existByEmail(final String email) {
    return accountRepository.existsByEmail(email);
  }
}
