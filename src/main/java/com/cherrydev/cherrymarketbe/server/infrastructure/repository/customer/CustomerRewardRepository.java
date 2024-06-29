package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerReward;

public interface CustomerRewardRepository extends JpaRepository<CustomerReward, Long> {

    List<CustomerReward> findAllByAccount(Account account);
}
