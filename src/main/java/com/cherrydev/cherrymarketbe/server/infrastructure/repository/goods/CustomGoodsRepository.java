package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsSearchConditions;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomGoodsRepository {

  Page<Goods> findByConditions(Pageable pageable, GoodsSearchConditions conditions);
}
