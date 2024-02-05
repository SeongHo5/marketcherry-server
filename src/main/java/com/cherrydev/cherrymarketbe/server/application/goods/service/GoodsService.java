package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.GoodsRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_GOODS;
import static com.cherrydev.cherrymarketbe.server.domain.goods.enums.SalesStatus.ON_SALE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    public static final int RETRY_LIMIT = 3;

    private final GoodsRepository goodsRepository;
    private final GoodsValidator goodsValidator;

    public Page<GoodsInfo> findAll(final Pageable pageable) {
        List<GoodsInfo> goodsList = goodsRepository.findAll()
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

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateGoodsInventory(List<Cart> cartItems) {
        cartItems.forEach(cart -> handleUpdateInventoryInternal(cart.getGoods(), cart.getQuantity()));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Goods fetchGoodsEntity(String goodsCode) {
        return goodsRepository.findByCode(goodsCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
    }

    private void handleUpdateInventoryInternal(Goods goods, int requestedQuantity) {
        int retryCount = 0;
        do {
            try {
                goods.updateInventory(requestedQuantity);
            } catch (OptimisticLockException ex) {
                retryCount++;
            }
        } while (retryCount < RETRY_LIMIT);
    }

}
