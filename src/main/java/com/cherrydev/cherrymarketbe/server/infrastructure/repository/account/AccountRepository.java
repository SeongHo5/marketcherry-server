package com.cherrydev.cherrymarketbe.server.infrastructure.repository.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    RegisterType getRegisterTypeByEmail(String email);

    boolean existsByEmail(String email);

    @Procedure(name = "ReleaseRestrictedAccount")
    void releaseRestrictedAccount();

    @Procedure(name = "DeleteInactiveAccount")
    void deleteInactiveAccount();
}
