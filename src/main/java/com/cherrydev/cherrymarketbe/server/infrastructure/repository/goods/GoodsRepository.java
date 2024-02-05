package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Optional<Goods> findByCode(String code);

}
