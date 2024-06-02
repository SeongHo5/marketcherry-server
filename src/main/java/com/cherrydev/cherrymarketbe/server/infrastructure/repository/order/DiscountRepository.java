package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByCode(String code);
}
