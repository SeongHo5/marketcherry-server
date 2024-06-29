package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Maker;

public interface MakerRepository extends JpaRepository<Maker, Long> {
}
