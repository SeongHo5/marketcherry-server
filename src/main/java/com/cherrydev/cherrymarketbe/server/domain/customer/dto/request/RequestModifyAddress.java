package com.cherrydev.cherrymarketbe.server.domain.customer.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class RequestModifyAddress {

    Long addressId;

    Boolean isDefault;

    String name;

    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자입니다.")
    String zipcode;

    String address;

    String addressDetail;
}

