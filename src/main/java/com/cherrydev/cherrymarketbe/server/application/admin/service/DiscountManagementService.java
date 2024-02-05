package com.cherrydev.cherrymarketbe.server.application.admin.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.RequestAddDiscount;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.RequestModifyDiscount;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.DiscountInfo;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_DISCOUNT;

@Service
@RequiredArgsConstructor
public class DiscountManagementService {

    private final DiscountRepository discountRepository;

    @Transactional(readOnly = true)
    public List<DiscountInfo> fetchAllDiscounts() {
        return discountRepository.findAll()
                .stream()
                .map(DiscountInfo::of)
                .toList();
    }

    @Transactional
    public void addDiscount(final RequestAddDiscount request) {
        discountRepository.save(Discount.of(request));
    }

    @Transactional
    public DiscountInfo modifyDiscountDetail(
            final String discountCode,
            final RequestModifyDiscount request
    ) {
        Discount discountInfo = fetchDiscountEntity(discountCode);
        return DiscountInfo.of(handleModifyInternal(discountInfo, request));
    }

    @Transactional
    public void deleteDiscount(final String discountCode){
        discountRepository.delete(fetchDiscountEntity(discountCode));
    }

    private Discount fetchDiscountEntity(final String discountCode) {
        return discountRepository.findByCode(discountCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_DISCOUNT));
    }

    private Discount handleModifyInternal(
            final Discount discount,
            final RequestModifyDiscount request
    ) {
        if (request.description() != null) {
            discount.updateDescription(request.description());
        }
        if (request.discountRate() != null) {
            discount.updateDiscountRate(request.discountRate());
        }
        if (request.startDate() != null) {
            discount.updateStartDate(LocalDate.parse(request.startDate()));
        }
        if (request.endDate() != null) {
            discount.updateEndDate(LocalDate.parse(request.endDate()));
        }
        return discount;
    }

}
