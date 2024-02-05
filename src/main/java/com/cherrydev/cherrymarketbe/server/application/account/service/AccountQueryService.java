package com.cherrydev.cherrymarketbe.server.application.account.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_ACCOUNT;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

    private final AccountRepository accountRepository;

    public Account fetchAccountEntity(final String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
    }

    public Page<Account> fetchAllAccountEntities(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }


    public RegisterType getRegisterTypeByEmail(final String email) {
        return accountRepository.getRegisterTypeByEmail(email);
    }

    public boolean existByEmail(final String email) {
        return accountRepository.existsByEmail(email);
    }


}
