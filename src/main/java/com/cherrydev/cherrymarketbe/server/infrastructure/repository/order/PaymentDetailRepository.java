package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
}
