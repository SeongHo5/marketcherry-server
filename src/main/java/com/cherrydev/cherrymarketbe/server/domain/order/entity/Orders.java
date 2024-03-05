package com.cherrydev.cherrymarketbe.server.domain.order.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@Comment("주문")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ORDERS", uniqueConstraints = {
        @UniqueConstraint(name = "ORDERS_UNIQUE", columnNames = {"ACNT_ID", "ORDERS_CODE"})
})
@SQLDelete(sql = "UPDATE ORDERS SET DELETED_DE = CURRENT_TIMESTAMP WHERE ORDERS_ID = ?")
public class Orders extends BaseEntity {

    @Id
    @Comment("주문 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDERS_ID", nullable = false)
    private Long id;

    @Comment("주문자")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ACNT_ID", nullable = false)
    private Account account;

    @Comment("주문 코드")
    @UuidGenerator
    @Column(name = "ORDERS_CODE", columnDefinition = "BINARY(16)", nullable = false)
    private UUID code;

    @Setter
    @Comment("주문 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "ORDERS_STTUS", nullable = false, length = 20)
    private OrderStatus status;

    @Comment("주문명")
    @Column(name = "ORDERS_NM", nullable = false, length = 100)
    private String name;

    @Comment("주문 취소 일시")
    @Column(name = "DELETED_DE")
    private Timestamp deletedAt;

    @Setter
    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private DeliveryDetail deliveryDetail;

    @Setter
    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private PaymentDetail paymentDetail;

    @Setter
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public static Orders from(Account account, String orderName) {
        return Orders.builder()
                .account(account)
                .status(OrderStatus.PENDING_PAYMENT)
                .name(orderName)
                .build();
    }


}
