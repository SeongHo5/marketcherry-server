package com.cherrydev.cherrymarketbe.server.domain.admin.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;
import lombok.Builder;

import static com.cherrydev.cherrymarketbe.server.application.common.utils.TimeFormatter.localDateToString;

@Builder
public record DiscountInfo(
        String code,
        String description,
        Integer discountRate,
        String startDate,
        String endDate
) {

    public static DiscountInfo of(Discount discount) {
        return DiscountInfo.builder()
                .code(discount.getCode())
                .description(discount.getDescription())
                .discountRate(discount.getDiscountRate())
                .startDate(localDateToString(discount.getStartDate()))
                .endDate(localDateToString(discount.getEndDate()))
                .build();
    }

}
