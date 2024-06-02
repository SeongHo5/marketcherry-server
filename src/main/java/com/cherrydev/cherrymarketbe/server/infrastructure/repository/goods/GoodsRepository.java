package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Optional<Goods> findByCode(String code);

}
