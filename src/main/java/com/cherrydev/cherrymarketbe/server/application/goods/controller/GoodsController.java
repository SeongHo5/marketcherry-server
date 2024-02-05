package com.cherrydev.cherrymarketbe.server.application.goods.controller;

import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/")
    public ResponseEntity<Page<GoodsInfo>> getListAll(Pageable pageable) {
        return ResponseEntity.ok(goodsService.findAll(pageable));
    }

    @DeleteMapping("/{goods_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("goods_id") final Long goodsId) {
        goodsService.deleteById(goodsId);
        return ResponseEntity.ok().build();
    }
}
