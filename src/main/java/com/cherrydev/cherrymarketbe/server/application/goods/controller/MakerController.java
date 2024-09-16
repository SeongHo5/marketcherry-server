package com.cherrydev.cherrymarketbe.server.application.goods.controller;

import com.cherrydev.cherrymarketbe.server.application.goods.service.MakerService;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.MakerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/makers")
public class MakerController {

  private final MakerService makerService;

  @GetMapping("/")
  public ResponseEntity<Page<MakerInfo>> fetchAllMakers(Pageable pageable) {
    return ResponseEntity.ok(makerService.fetchAllMakers(pageable));
  }
}
