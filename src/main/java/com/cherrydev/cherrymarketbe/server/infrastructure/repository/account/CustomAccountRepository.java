package com.cherrydev.cherrymarketbe.server.infrastructure.repository.account;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.AccountSearchConditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomAccountRepository {

    Page<Account> findByConditions(Pageable pageable, AccountSearchConditions conditions);

}
