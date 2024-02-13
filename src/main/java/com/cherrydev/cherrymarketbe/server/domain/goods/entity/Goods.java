package com.cherrydev.cherrymarketbe.server.domain.goods.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.RequestAddGoods;
import com.cherrydev.cherrymarketbe.server.domain.goods.enums.SalesStatus;
import com.cherrydev.cherrymarketbe.server.domain.goods.enums.StorageType;
import com.cherrydev.cherrymarketbe.server.domain.goods.enums.VolumeType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@Comment("상품")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "GOODS", uniqueConstraints = {
        @UniqueConstraint(name = "GOODS_CODE_UNIQUE", columnNames = {"GOODS_CODE"})
})
public class Goods extends BaseEntity {

    @Id
    @Comment("상품 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GOODS_ID", nullable = false)
    private Long id;

    @Comment("상품 코드")
    @Column(name = "GOODS_CODE", nullable = false, length = 20)
    private String code;

    @Comment("상품 제조사 ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MAKR_ID", nullable = false)
    private Maker maker;

    @Comment("상품 카테고리 ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CTGRY_ID", nullable = false)
    private Category category;

    @Comment("상품 이름")
    @Column(name = "GOODS_NM", nullable = false, length = 50)
    private String name;

    @Comment("상품 설명")
    @Column(name = "GOODS_DC", nullable = false, length = 80)
    private String description;

    @Nullable
    @Comment("상품 할인 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "GOODS_DSCNT_ID")
    private Discount discount;

    @Comment("상품 가격")
    @Column(name = "GOODS_PRICE", nullable = false)
    private Integer price;

    @Comment("상품 구매 가격")
    @Column(name = "GOODS_SPLPC", nullable = false)
    private Integer purchasedPrice;

    @Comment("상품 재고")
    @Column(name = "GOODS_INVTRY", nullable = false)
    private Integer inventory;

    @Comment("상품 보관 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "GOODS_CSTDY_TY", nullable = false, length = 30)
    private StorageType storageType;

    @Comment("상품 용량 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "GOODS_CPCY", nullable = false, length = 10)
    private VolumeType volumeType;

    @Comment("상품 유툥기한")
    @Column(name = "GOODS_EXPR_DE", nullable = false, length = 30)
    private String expireAt;

    @Lob
    @Comment("상품 알레르기 정보")
    @Column(name = "GOODS_ALLERGY_INFO")
    private String allergyInfo;

    @Comment("상품 원산지")
    @Column(name = "GOODS_ORGPLCE", nullable = false, length = 100)
    private String originPlace;

    @Comment("상품 판매 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "GOODS_SEL_STTUS", nullable = false, length = 20)
    private SalesStatus salesStatus;

    public static Goods of(RequestAddGoods request) {
        return Goods.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .purchasedPrice(request.getRetailPrice())
                .inventory(request.getInventory())
                .storageType(StorageType.valueOf(request.getStorageType()))
                .volumeType(VolumeType.valueOf(request.getCapacity()))
                .expireAt(request.getExpDate())
                .allergyInfo(request.getAllergyInfo())
                .originPlace(request.getOriginPlace())
                .salesStatus(SalesStatus.valueOf(request.getSalesStatus()))
                .build();
    }

    public void updateInventory(int requestedQuantity) {
        this.inventory -= requestedQuantity;
    }

    public int getDiscountedPrice() {
        return discount == null
                ? price
                : (int) (price * (1 - discount.getDiscountRate() / 100.0));
    }

    public int getDiscountRate() {
        return discount == null
                ? 0
                : discount.getDiscountRate();
    }

}
