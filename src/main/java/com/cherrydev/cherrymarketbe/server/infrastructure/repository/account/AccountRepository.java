package com.cherrydev.cherrymarketbe.server.infrastructure.repository.account;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Page<Account> findAll(Pageable pageable);

    RegisterType getRegisterTypeByEmail(String email);

    boolean existsByEmail(String email);

    @Procedure(name = "ReleaseRestrictedAccount")
    void releaseRestrictedAccount();

    @Procedure(name = "DeleteInactiveAccount")
    void deleteInactiveAccount();
}
