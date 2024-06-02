package com.cherrydev.cherrymarketbe.server.infrastructure.repository.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.AccountSearchConditions;

public interface CustomAccountRepository {

    Page<Account> findByConditions(Pageable pageable, AccountSearchConditions conditions);
}
