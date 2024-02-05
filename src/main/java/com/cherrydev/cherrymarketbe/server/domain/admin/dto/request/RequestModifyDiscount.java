package com.cherrydev.cherrymarketbe.server.domain.admin.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RequestModifyDiscount(
        @Pattern(regexp = "^[1-9]{1,2}", message = "할인율은 1~99 사이의 숫자로 입력해 주세요")
        Integer discountRate,
        @NotBlank(message = "할인에 대한 설명은 공백일 수 없습니다.")
        String description,
        @Future(message = "시작일은 현재 시간 이후로 입력해 주세요")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "시작일은 YYYY-MM-DD 형식으로 입력해 주세요")
        String startDate,
        @Future(message = "종료일은 현재 시간 이후로 입력해 주세요")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "종료일은 YYYY-MM-DD 형식으로 입력해 주세요")
        String endDate
) {
}
