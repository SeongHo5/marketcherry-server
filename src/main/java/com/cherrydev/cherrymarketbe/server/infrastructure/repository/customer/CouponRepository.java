package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findById(Long id);
    Optional<Coupon> findByCode(String code);

    Page<Coupon> findAll(Pageable pageable);

    boolean existsByCode(String code);
}
