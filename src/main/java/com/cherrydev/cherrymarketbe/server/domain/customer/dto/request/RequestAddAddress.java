package com.cherrydev.cherrymarketbe.server.domain.customer.dto.request;

public record RequestAddAddress(
        Boolean isDefault,
        String name,
        String zipcode,
        String address,
        String addressDetail) {

}
