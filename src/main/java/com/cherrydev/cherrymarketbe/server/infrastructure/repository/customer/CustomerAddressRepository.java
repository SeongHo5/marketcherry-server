package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    Optional<CustomerAddress> findByIdAndAccount(Long id, Account account);

    Optional<CustomerAddress> findByAccountAndIsDefault(Account account, Boolean isDefault);

    List<CustomerAddress> findAllByAccount(Account account);

    Integer countAllByAccount(Account account);

    boolean existsByAccountAndIsDefault(Account account, Boolean isDefault);
}
