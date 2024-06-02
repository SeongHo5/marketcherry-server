package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
