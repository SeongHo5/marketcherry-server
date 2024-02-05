package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
