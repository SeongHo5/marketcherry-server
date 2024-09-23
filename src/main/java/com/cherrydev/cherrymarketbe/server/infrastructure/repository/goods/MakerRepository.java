package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Maker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakerRepository extends JpaRepository<Maker, Long> {}
