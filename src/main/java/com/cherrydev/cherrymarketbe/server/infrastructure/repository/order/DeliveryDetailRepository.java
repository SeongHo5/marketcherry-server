package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Long> {
}
