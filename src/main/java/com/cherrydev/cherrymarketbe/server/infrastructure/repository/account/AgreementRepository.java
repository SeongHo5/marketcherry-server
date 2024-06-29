package com.cherrydev.cherrymarketbe.server.infrastructure.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Agreement;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
