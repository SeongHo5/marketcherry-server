package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.exception.NotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.NOT_FOUND_GOODS;
import static com.cherrydev.cherrymarketbe.server.domain.goods.enums.SalesStatus.ON_SALE;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final CustomGoodsRepository customGoodsRepository;
    private final GoodsValidator goodsValidator;
    private final GoodsInventoryService inventoryService;

    @Transactional(readOnly = true)
    public Page<GoodsInfo> fetchGoodsByConditions(Pageable pageable, GoodsSearchConditions conditions) {
        List<GoodsInfo> goodsList = customGoodsRepository.findByConditions(pageable, conditions)
                .stream()
                .map(GoodsInfo::of)
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
    public void updateGoodsInventory(List<Cart> cartItems) {
        cartItems.forEach(cartItem -> {
            Goods goods = cartItem.getGoods();
            int requestedQuantity = cartItem.getQuantity();
            inventoryService.processInventoryUpdate(goods, requestedQuantity);
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Goods fetchGoodsEntity(String goodsCode) {
        return goodsRepository.findByCode(goodsCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
    }
}
