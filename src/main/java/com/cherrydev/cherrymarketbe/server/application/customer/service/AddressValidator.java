package com.cherrydev.cherrymarketbe.server.application.customer.service;

import static com.cherrydev.cherrymarketbe.server.application.customer.service.AddressService.MAX_ADDRESS_COUNT;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.*;

import com.cherrydev.cherrymarketbe.server.application.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestModifyAddress;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CustomerAddressRepository;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressValidator {

  private final CustomerAddressRepository addressRepository;

  protected void checkDefaultAddressAlreadyExists(final Account account) {
    boolean isExist = addressRepository.existsByAccountAndIsDefault(account, true);
    if (isExist) {
      throw new ServiceFailedException(DEFAULT_ADDRESS_ALREADY_EXISTS);
    }
  }

  protected void verifyAddressLimit(final Account account) {
    int currentAddressCount = addressRepository.countAllByAccount(account);
    if (currentAddressCount >= MAX_ADDRESS_COUNT) {
      throw new ServiceFailedException(ADDRESS_COUNT_EXCEEDED);
    }
  }

  protected void checkIfRequestBodyIsAllNull(final RequestModifyAddress requestModifyAddress) {
    boolean isAllNull =
        Stream.of(
                requestModifyAddress.getIsDefault(),
                requestModifyAddress.getName(),
                requestModifyAddress.getZipcode(),
                requestModifyAddress.getAddress(),
                requestModifyAddress.getAddressDetail())
            .allMatch(Objects::isNull);

    if (isAllNull) {
      throw new ServiceFailedException(INVALID_INPUT_VALUE);
    }
  }
}
