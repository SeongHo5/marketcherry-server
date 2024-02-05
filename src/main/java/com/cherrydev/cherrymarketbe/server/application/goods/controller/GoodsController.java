package com.cherrydev.cherrymarketbe.server.application.goods.controller;

import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

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
    public ResponseEntity<Void> delete(@PathVariable("goods_id") final Long goodsId) {
        goodsService.deleteById(goodsId);
        return ResponseEntity.ok().build();
    }
}
