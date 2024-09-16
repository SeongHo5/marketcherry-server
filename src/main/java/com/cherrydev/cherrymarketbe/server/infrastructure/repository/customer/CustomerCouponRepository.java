package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCouponRepository extends JpaRepository<CustomerCoupon, Long> {

  List<CustomerCoupon> findAllByAccount(Account account);

  boolean existsByAccountAndCoupon(Account account, Coupon coupon);
}
