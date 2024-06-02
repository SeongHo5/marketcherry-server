package com.cherrydev.cherrymarketbe.server.domain.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@Comment("장바구니")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CART", uniqueConstraints = {
        @UniqueConstraint(name = "CART_ITEM_UNIQUE", columnNames = {"ACNT_ID", "GOODS_ID"})
})
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ACNT_ID", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "GOODS_ID", nullable = false)
    private Goods goods;

    @NotNull @Column(name = "CART_QY", nullable = false)
    private Integer quantity;

    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        this.quantity--;
    }

    public static Cart from(Account account, Goods goods) {
        return Cart.builder()
                .account(account)
                .goods(goods)
                .quantity(1)
                .build();
    }

}
