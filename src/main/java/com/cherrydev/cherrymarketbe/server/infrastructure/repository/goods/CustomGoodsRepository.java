package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsSearchConditions;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;

public interface CustomGoodsRepository {

    Page<Goods> findByConditions(Pageable pageable, GoodsSearchConditions conditions);

}
