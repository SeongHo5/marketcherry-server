package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import java.util.List;
import jakarta.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsSearchConditions;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.QGoods;
import com.cherrydev.cherrymarketbe.server.domain.goods.enums.GoodsSortType;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class GoodsRepositoryImpl implements CustomGoodsRepository {

    private static final QGoods qGoods = QGoods.goods;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Goods> findByConditions(Pageable pageable, GoodsSearchConditions conditions) {
        // 쿼리 생성 및 엔티티 조회
        List<Goods> result = jpaQueryFactory
                .selectFrom(qGoods)
                .where(
                        nameLike(conditions.goodsName()),
                        categoryIdEquals(conditions.categoryId()),
                        makerIdEquals(conditions.makerId()),
                        filterOnlyDiscountedItems(conditions.isOnDiscount())
                )
                .orderBy(determineOrderSpecification(conditions.sort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory
                .select(qGoods.count())
                .from(qGoods)
                .where(
                        nameLike(conditions.goodsName()),
                        categoryIdEquals(conditions.categoryId()),
                        makerIdEquals(conditions.makerId()),
                        filterOnlyDiscountedItems(conditions.isOnDiscount())
                ).fetchFirst();

        return new PageImpl<>(result, pageable, totalCount);
    }

    private BooleanExpression nameLike(@Nullable String name) {
        if (StringUtils.hasText(name)) {
            return qGoods.name.likeIgnoreCase("%" + name + "%");
        }
        return null;
    }

    private BooleanExpression categoryIdEquals(@Nullable Long categoryId) {
        if (categoryId != null) {
            return qGoods.category.id.eq(categoryId);
        }
        return null;
    }

    private BooleanExpression makerIdEquals(@Nullable Long makerId) {
        if (makerId != null) {
            return qGoods.maker.id.eq(makerId);
        }
        return null;
    }

    private BooleanExpression filterOnlyDiscountedItems(@Nullable Boolean isOnDiscount) {
        if (isOnDiscount != null) {
            return isOnDiscount
                    ? qGoods.discount.isNotNull()
                    : null;
        }
        return null;
    }

    private OrderSpecifier<?> determineOrderSpecification(@Nullable String sort) {
        // 기본 정렬은 최신 등록 순
        OrderSpecifier<?> defaultOrder = qGoods.createdAt.asc();

        if (sort != null) {
            if (GoodsSortType.NEWEST.isEquals(sort)) {
                return qGoods.createdAt.asc();
            }
            if (GoodsSortType.OLDEST.isEquals(sort)) {
                return qGoods.createdAt.desc();
            }
            if (GoodsSortType.PRICE_ASC.isEquals(sort)) {
                return qGoods.price.asc();
            }
            if (GoodsSortType.PRICE_DESC.isEquals(sort)) {
                return qGoods.price.desc();
            }
        }
        return defaultOrder;
    }
}
