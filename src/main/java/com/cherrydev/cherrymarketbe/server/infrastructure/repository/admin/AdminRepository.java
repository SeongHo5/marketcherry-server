package com.cherrydev.cherrymarketbe.server.infrastructure.repository.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.QGoods;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.QOrderDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.QOrders;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private static final QGoods qGoods = QGoods.goods;
    private static final QOrders qOrders = QOrders.orders;
    private static final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;

    private final JPAQueryFactory jpaQueryFactory;

    // 특정연도, 월에 N번 이상 주문된 상품 목록
    public Page<Goods> findGoodsByYearAndMonth(Pageable pageable, int year, int month, int count) {
        List<Goods> goodsList = jpaQueryFactory
                .select(qGoods)
                .from(qOrders)
                .join(qOrders.orderDetails, qOrderDetail)
                .join(qOrderDetail.goods, qGoods)
                .where(qOrders.createdAt.year().eq(year)
                        .and(qOrders.createdAt.month().eq(month)))
                .groupBy(qGoods)
                .having(qOrderDetail.count().goe(count))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory
                .select(qGoods.count())
                .from(qOrders)
                .join(qOrders.orderDetails, qOrderDetail)
                .join(qOrderDetail.goods, qGoods)
                .where(qOrders.createdAt.year().eq(year)
                        .and(qOrders.createdAt.month().eq(month)))
                .groupBy(qGoods)
                .having(qOrderDetail.count().goe(count))
                .fetchFirst();

        return new PageImpl<>(goodsList, pageable, totalCount);
    }


}
