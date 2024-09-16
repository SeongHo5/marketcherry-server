package com.cherrydev.cherrymarketbe.server.domain.customer.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import lombok.Builder;

@Builder
public record AddressInfo(
    Long addressId,
    Boolean isDefault,
    String name,
    String zipcode,
    String address,
    String addressDetail,
    String createdAt) {

  public static AddressInfo of(CustomerAddress customerAddress) {
    return AddressInfo.builder()
        .addressId(customerAddress.getId())
        .isDefault(customerAddress.getIsDefault())
        .name(customerAddress.getName())
        .zipcode(customerAddress.getZipCode())
        .address(customerAddress.getRoadNameAddress())
        .addressDetail(customerAddress.getAddressDetail())
        .createdAt(customerAddress.getCreatedAt().toString())
        .build();
  }
}
