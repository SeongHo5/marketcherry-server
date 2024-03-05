package com.cherrydev.cherrymarketbe.server.domain.order.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.request.RequestCreateOrder;
import com.cherrydev.cherrymarketbe.server.domain.order.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Comment("배송 상세")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "DLVY_DETAIL")
public class DeliveryDetail extends BaseEntity {

    @Id
    @Comment("배송 상세 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DLVY_DETAIL_ID", nullable = false)
    private Long id;

    @Comment("주문 ID")
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ORDERS_ID", nullable = false, referencedColumnName = "ORDERS_ID")
    private Orders orders;

    @Comment("배송 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "DLVY_STTUS", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Comment("배송 수령자")
    @Column(name = "DLVY_RECPTR", nullable = false)
    private String recipient;

    @Comment("배송 수령자 연락처")
    @Column(name = "DLVY_RECPTR_CTTPC", nullable = false)
    private String recipientContact;

    @Comment("배송 우편번호")
    @Column(name = "DLVY_ZIP", nullable = false)
    private String zipCode;

    @Comment("배송 도로명 주소")
    @Column(name = "DLVY_ADRES_RDNM", nullable = false)
    private String roadNameAddress;

    @Comment("배송 상세 주소")
    @Column(name = "DLVY_ADRES_DETAIL", nullable = false)
    private String addressDetail;

    @Comment("배송 장소")
    @Column(name = "DLVY_ADRES_PLACE")
    private String deliveryPlace;

    @Comment("배송 요청 사항")
    @Column(name = "DLVY_ADRES_CMG")
    private String deliveryComment;

    @Comment("배송 요청 사항 상세")
    @Column(name = "DLVY_ADRES_REQUEST")
    private String deliveryRequest;

    @Comment("배송 주소 변경 여부")
    @Column(name = "CHECK_CHANGE_ADRES")
    private Boolean isAddressChanged;

    public static DeliveryDetail from(Orders orders, RequestCreateOrder request) {
        return DeliveryDetail.builder()
                .orders(orders)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
                .recipient(request.recipient())
                .recipientContact(request.recipientContact())
                .zipCode(request.zipCode())
                .roadNameAddress(request.address())
                .addressDetail(request.addressDetail())
                .deliveryPlace(request.deliveryPlace())
                .deliveryComment(request.deliveryComment())
                .build();
    }

    public static DeliveryDetail from(Orders orders, CustomerAddress customerAddress, RequestCreateOrder request ) {
        return DeliveryDetail.builder()
                .orders(orders)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
                .recipient(customerAddress.getName())
                .recipientContact(orders.getAccount().getContact())
                .zipCode(customerAddress.getZipCode())
                .roadNameAddress(customerAddress.getRoadNameAddress())
                .addressDetail(customerAddress.getAddressDetail())
                .deliveryPlace(request.deliveryPlace())
                .deliveryComment(request.deliveryComment())
                .build();

    }

}
