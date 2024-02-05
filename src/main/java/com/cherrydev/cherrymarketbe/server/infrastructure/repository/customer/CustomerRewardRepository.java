package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRewardRepository extends JpaRepository<CustomerReward, Long> {

    List<CustomerReward> findAllByAccount(Account account);
}
