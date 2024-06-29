package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import lombok.Builder;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Maker;

@Builder
public record MakerInfo(
        String name,
        String businessNumber,
        String contact,
        String email
) {

    public static MakerInfo of(Maker maker) {
        return MakerInfo.builder()
                .name(maker.getName())
                .businessNumber(maker.getBuisnessNumber())
                .contact(maker.getContact())
                .email(maker.getEmail())
                .build();
    }

}
