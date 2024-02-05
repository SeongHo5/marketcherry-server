package com.cherrydev.cherrymarketbe.server.application.admin.controller;

import com.cherrydev.cherrymarketbe.server.application.admin.service.DiscountManagementService;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.RequestAddDiscount;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.RequestModifyDiscount;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.DiscountInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class DiscountManagementController {

    private final DiscountManagementService discountManagementService;

    @GetMapping("/")
    public ResponseEntity<List<DiscountInfo>> fetchAllDiscounts() {
        return ResponseEntity.ok(
                discountManagementService.fetchAllDiscounts()
        );
    }

    @PostMapping("/")
    public ResponseEntity<Void> addDiscount(
            @RequestBody @Valid final RequestAddDiscount request
    ) {
        discountManagementService.addDiscount(request);
        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping("/{discount_code}")
    public ResponseEntity<DiscountInfo> updateDiscount(
            @PathVariable("discount_code") final String discountCode,
            @RequestBody @Valid final RequestModifyDiscount request
    ) {
        return ResponseEntity.ok(
                discountManagementService.modifyDiscountDetail(discountCode, request)
        );
    }

    @DeleteMapping("/{discount_code}")
    public ResponseEntity<Void> deleteDiscount(
            @PathVariable("discount_code") final String discountCode
    ) {
        discountManagementService.deleteDiscount(discountCode);
        return ResponseEntity.ok().build();
    }

}
