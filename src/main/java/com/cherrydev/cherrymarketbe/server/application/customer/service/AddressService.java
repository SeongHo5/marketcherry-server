package com.cherrydev.cherrymarketbe.server.application.customer.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddAddress;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestModifyAddress;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.response.AddressInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CustomerAddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_ADDRESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final CustomerAddressRepository addressRepository;
    private final AddressValidator addressValidator;


    protected static final int MAX_ADDRESS_COUNT = 3;

    @Transactional
    public void addAddress(
            final AccountDetails accountDetails,
            final RequestAddAddress request
    ) {
        Account account = accountDetails.getAccount();
        CustomerAddress customerAddress = CustomerAddress.from(request, account);

        addressValidator.verifyAddressLimit(account);
        addressValidator.checkDefaultAddressAlreadyExists(account);

        addressRepository.save(customerAddress);
    }

    @Transactional(readOnly = true)
    public List<AddressInfo> getAddress(
            final AccountDetails accountDetails
    ) {
        Account account = accountDetails.getAccount();
        return addressRepository.findAllByAccount(account)
                .stream()
                .map(AddressInfo::of)
                .toList();
    }
    @Transactional(readOnly = true)
    public CustomerAddress fetchDefaultAddressForOrders(final Account account) {
        return addressRepository.findByAccountAndIsDefault(account, true)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ADDRESS));
    }

    @Transactional
    public void deleteAddress(
            final AccountDetails accountDetails,
            final Long addressId
    ) {
        Account account = accountDetails.getAccount();
        CustomerAddress customerAddress = fetchCustomerAddressEntity(addressId, account);

        addressRepository.delete(customerAddress);
    }

    @Transactional
    public AddressInfo modifyAddress(
            final AccountDetails accountDetails,
            final RequestModifyAddress requestModifyAddress
    ) {
        Account account = accountDetails.getAccount();
        Long addressId = requestModifyAddress.getAddressId();
        boolean isSetAsDefault = requestModifyAddress.getIsDefault();

        addressValidator.checkIfRequestBodyIsAllNull(requestModifyAddress);
        CustomerAddress customerAddress = fetchCustomerAddressEntity(addressId, account);

        if (isSetAsDefault) {
            addressValidator.checkDefaultAddressAlreadyExists(account);
        }
        CustomerAddress updatedAddress = handleModifyAddressInternal(customerAddress, requestModifyAddress);

        return AddressInfo.of(updatedAddress);
    }

    // ==================== PRIVATE METHODS ==================== //

    private CustomerAddress fetchCustomerAddressEntity(final Long addressId, final Account account) {
        return addressRepository.findByIdAndAccount(addressId, account)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ADDRESS));
    }

    private CustomerAddress handleModifyAddressInternal(
            final CustomerAddress customerAddress,
            final RequestModifyAddress requestModifyAddress) {
        if (requestModifyAddress.getIsDefault() != null) {
            customerAddress.updateDefaultAddress(requestModifyAddress.getIsDefault());
        }
        if (requestModifyAddress.getName() != null) {
            customerAddress.updateName(requestModifyAddress.getName());
        }
        if (requestModifyAddress.getZipcode() != null) {
            customerAddress.updateZipCode(requestModifyAddress.getZipcode());
        }
        if (requestModifyAddress.getAddress() != null) {
            customerAddress.updateRoadNameAddress(requestModifyAddress.getAddress());
        }
        if (requestModifyAddress.getAddressDetail() != null) {
            customerAddress.updateAddressDetail(requestModifyAddress.getAddressDetail());
        }
        return customerAddress;
    }

}
