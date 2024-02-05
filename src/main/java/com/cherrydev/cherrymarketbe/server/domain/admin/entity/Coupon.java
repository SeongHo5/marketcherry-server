package com.cherrydev.cherrymarketbe.server.domain.admin.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.IssueCoupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.enums.CouponType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;


@Entity
@Getter
@Builder
@Comment("쿠폰")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "COUPON")
public class Coupon extends BaseEntity {

    @Id
    @Comment("쿠폰 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUPON_ID", nullable = false)
    private Long id;

    @Comment("쿠폰 코드")
    @Column(name = "COUPON_CODE", nullable = false, length = 20)
    private String code;

    @Comment("쿠폰 이름")
    @Enumerated(EnumType.STRING)
    @Column(name = "COUPON_TY", nullable = false, length = 20)
    private CouponType type;

    @Comment("쿠폰 사용 최소 금액")
    @Column(name = "COUPON_MIN_AMOUNT", nullable = false)
    private Integer minimumAmount;

    @Comment("쿠폰 할인율")
    @Column(name = "COUPON_DSCNT_RATE", nullable = false)
    private Integer discountRate;

    @Comment("쿠폰 사용 가능일")
    @Column(name = "COUPON_BGNDE", nullable = false)
    private LocalDate startDate;

    @Comment("쿠폰 만료일")
    @Column(name = "COUPON_ENDDE", nullable = false)
    private LocalDate endDate;

    public static Coupon of(IssueCoupon request) {
        return Coupon.builder()
                .code(request.getCode())
                .type(CouponType.valueOf(request.getType()))
                .minimumAmount(request.getMinimumOrderAmount())
                .discountRate(request.getDiscountRate())
                .startDate(LocalDate.parse(request.getStartDate()))
                .endDate(LocalDate.parse(request.getEndDate()))
                .build();
    }

}
