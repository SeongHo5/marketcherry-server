package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.GoodsRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

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

    @Transactional(propagation = Propagation.REQUIRED)
    @Retryable(retryFor = {OptimisticLockException.class}, backoff = @Backoff(delay = 100, maxDelay = 500, multiplier = 2))
    public void updateGoodsInventory(List<Cart> cartItems) {
        cartItems.forEach(cartItem -> {
            Goods goods = cartItem.getGoods();
            int requestedQuantity = cartItem.getQuantity();
            if (goods.getInventory() < requestedQuantity) {
                throw new InsufficientStockException(INSUFFICIENT_STOCK, goods.getName());
            }

            handleUpdateInventoryInternal(goods, requestedQuantity);
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Goods fetchGoodsEntity(String goodsCode) {
        return goodsRepository.findByCode(goodsCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
    }

    private void handleUpdateInventoryInternal(Goods goods, int requestedQuantity) {
        goods.updateInventory(requestedQuantity);

    }
}
