package com.cherrydev.cherrymarketbe.server.application.customer.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cherrydev.cherrymarketbe.server.application.customer.service.AddressService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddAddress;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestModifyAddress;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.response.AddressInfo;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/address")
public class AddressController {

    private final AddressService addressService;

    /**
     * 배송지 추가
     * @param accountDetails 로그인한 계정 정보
     * @param requestAddAddress 추가할 배송지 정보
     */
    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody RequestAddAddress requestAddAddress
    ) {
        addressService.addAddress(accountDetails, requestAddAddress);
        return ResponseEntity.status(CREATED).build();
    }

    /**
     * 배송지 목록 가져오기
     * @param accountDetails 로그인한 계정 정보
     * @return 배송지 목록
     */
    @GetMapping("/lists")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AddressInfo>> getMyAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return ResponseEntity.ok(addressService.getAddress(accountDetails));
    }

    /**
     * 배송지 수정
     * @param accountDetails 로그인한 계정 정보
     * @param requestModifyAddress 수정할 배송지 정보
     */
    @PatchMapping("/")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<AddressInfo> modifyAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody RequestModifyAddress requestModifyAddress
    ) {
        return ResponseEntity.ok(addressService.modifyAddress(accountDetails, requestModifyAddress));
    }

    /**
     * 배송지 삭제
     * @param accountDetails 로그인한 계정 정보
     * @param addressId 삭제할 배송지 ID
     */
    @DeleteMapping("/")
     @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void deleteAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestParam Long addressId
    ) {
        addressService.deleteAddress(accountDetails, addressId);
    }




}
