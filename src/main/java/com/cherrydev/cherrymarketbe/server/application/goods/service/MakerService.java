package com.cherrydev.cherrymarketbe.server.application.goods.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cherrydev.cherrymarketbe.server.domain.goods.dto.MakerInfo;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.MakerRepository;

@Service
@RequiredArgsConstructor
public class MakerService {

    private final MakerRepository makerRepository;

    @Transactional(readOnly = true)
    public Page<MakerInfo> fetchAllMakers(Pageable pageable) {
        List<MakerInfo> makerInfoList = makerRepository.findAll(pageable)
                .stream()
                .map(MakerInfo::of)
                .toList();
        return new PageImpl<>(makerInfoList, pageable, makerInfoList.size());
    }

}
