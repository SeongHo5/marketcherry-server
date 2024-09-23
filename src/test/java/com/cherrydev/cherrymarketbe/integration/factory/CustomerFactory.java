package com.cherrydev.cherrymarketbe.integration.factory;

import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddAddress;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestModifyAddress;

public class CustomerFactory {

  public static RequestAddAddress createAddAddressRequestDtoA() {
    return new RequestAddAddress(false, "name", "51234", "address 1", "addressDetail 1");
  }

  public static RequestAddAddress createAddAddressRequestDtoB() {
    return new RequestAddAddress(true, "name", "51234", "address 1", "addressDetail 1");
  }

  public static RequestModifyAddress createModifyAddressRequestDtoA() {
    return new RequestModifyAddress(1L, false, "name", "51234", "address 1", "addressDetail 1");
  }
}
