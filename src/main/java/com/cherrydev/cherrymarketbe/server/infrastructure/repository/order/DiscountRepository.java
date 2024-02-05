package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByCode(String code);
}
