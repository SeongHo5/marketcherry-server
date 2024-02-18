package com.cherrydev.cherrymarketbe.server.application.goods.controller;

import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsSearchConditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;
    @GetMapping("")
    @Cacheable(value = "goodsCache", key = "#sort", condition = "#sort != null && #goodsName == null && #categoryId == null && #makerId == null && #isOnDiscount == null")
    public ResponseEntity<Page<GoodsInfo>> fetchGoodsByConditions(
            Pageable pageable,
            @RequestParam(value = "goods_name", required = false) final String goodsName,
            @RequestParam(value = "category_id", required = false) final Long categoryId,
            @RequestParam(value = "maker_id", required = false) final Long makerId,
            @RequestParam(value = "on_discount", required = false) final Boolean isOnDiscount,
            @RequestParam(value = "sort", required = false) final String sort
    ) {
        GoodsSearchConditions conditions = new GoodsSearchConditions(goodsName, categoryId, makerId, isOnDiscount, sort);
        return ResponseEntity.ok(goodsService.fetchGoodsByConditions(pageable, conditions));
    }

    @DeleteMapping("/{goods_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("goods_id") final Long goodsId) {
        goodsService.deleteById(goodsId);
        return ResponseEntity.ok().build();
    }
}
