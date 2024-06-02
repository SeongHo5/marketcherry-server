package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllByAccount(Account account);

    Optional<Orders> findByCode(UUID orderCode);

    boolean existsByCode(UUID orderCode);

}
