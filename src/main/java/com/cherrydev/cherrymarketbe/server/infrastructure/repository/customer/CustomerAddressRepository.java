package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    Optional<CustomerAddress> findByIdAndAccount(Long id, Account account);

    List<CustomerAddress> findAllByAccount(Account account);

    Integer countAllByAccount(Account account);

    boolean existsByAccountAndIsDefault(Account account, Boolean isDefault);
}
