package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
}
