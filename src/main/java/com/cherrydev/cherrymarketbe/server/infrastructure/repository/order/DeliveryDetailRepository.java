package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Long> {
}
