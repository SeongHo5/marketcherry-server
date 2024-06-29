package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
