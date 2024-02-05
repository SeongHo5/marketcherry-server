package com.cherrydev.cherrymarketbe.server.infrastructure.repository.account;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
