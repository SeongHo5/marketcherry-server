package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.annotation.DistributedLock;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.CouldNotObtainLockException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsSearchConditions;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.CustomGoodsRepository;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INSUFFICIENT_STOCK;
import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_GOODS;
import static com.cherrydev.cherrymarketbe.server.domain.goods.enums.SalesStatus.ON_SALE;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final CustomGoodsRepository customGoodsRepository;
    private final GoodsValidator goodsValidator;

    @Transactional(readOnly = true)
    public Page<GoodsInfo> fetchGoodsByConditions(Pageable pageable, GoodsSearchConditions conditions) {
        List<GoodsInfo> goodsList = customGoodsRepository.findByConditions(pageable, conditions)
                .stream()
                .map(goods -> GoodsInfo.of(goods, goods.getDiscount()))
                .toList();
        return new PageImpl<>(goodsList, pageable, goodsList.size());
    }

    @Transactional
    public void deleteById(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
        goodsValidator.validateGoodsSalesStatus(goods, ON_SALE);
        goodsRepository.delete(goods);
    }
    // 왜 updateGoodsInventory에서는 락이 적용되고, handleUpdateInventoryInternal에선 안돼
    @Transactional(propagation = Propagation.REQUIRED)
    @DistributedLock(keyName = "${goods.code}")
    @Retryable(retryFor = {CouldNotObtainLockException.class}, backoff = @Backoff(delay = 100, maxDelay = 500, multiplier = 2))
    public void updateGoodsInventory(List<Cart> cartItems) {
        cartItems.forEach(cartItem -> {
            Goods goods = cartItem.getGoods();
            int requestedQuantity = cartItem.getQuantity();

            if (goods.getInventory() < requestedQuantity) {
                throw new InsufficientStockException(INSUFFICIENT_STOCK, goods.getName());
            }

            goods.updateInventory(requestedQuantity);
            log.info("재고 업데이트 완료! / 요청 수량 : {}, 반영 후 재고 : {}", requestedQuantity, goods.getInventory());
            goodsRepository.saveAndFlush(goods);
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Goods fetchGoodsEntity(String goodsCode) {
        return goodsRepository.findByCode(goodsCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
    }
}
