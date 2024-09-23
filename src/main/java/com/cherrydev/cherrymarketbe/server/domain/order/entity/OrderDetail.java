package com.cherrydev.cherrymarketbe.server.domain.order.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@Comment("주문 상세")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "ORDER_DETAIL",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "ORDER_DETAIL_UNIQUE",
          columnNames = {"ORDERS_ID", "GOODS_ID"})
    })
public class OrderDetail extends BaseEntity {

  @Id
  @Comment("주문 상세 ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("주문 ID")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "ORDERS_ID", nullable = false, referencedColumnName = "ORDERS_ID")
  private Orders orders;

  @Comment("상품 ID")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "GOODS_ID", nullable = false, referencedColumnName = "GOODS_ID")
  private Goods goods;

  @Comment("주문 수량")
  @Column(name = "ORDERS_QY", nullable = false)
  private Integer orderQuantity;

  public static OrderDetail from(Orders orders, Cart cart) {
    return OrderDetail.builder()
        .orders(orders)
        .goods(cart.getGoods())
        .orderQuantity(cart.getQuantity())
        .build();
  }
}
