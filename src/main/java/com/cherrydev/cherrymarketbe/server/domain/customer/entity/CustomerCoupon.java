package com.cherrydev.cherrymarketbe.server.domain.customer.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@Comment("고객 쿠폰")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "CSTMR_COUPON",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "ONE_COUPON_PER_ACCOUNT",
          columnNames = {"ACNT_ID", "COUPON_ID"})
    })
public class CustomerCoupon extends BaseEntity {

  @Id
  @Comment("고객 쿠폰 ID")
  @Column(name = "CSTMR_COUPON_ID", nullable = false)
  private Long id;

  @Comment("고객 ID")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "ACNT_ID", nullable = false)
  private Account account;

  @Comment("쿠폰 ID")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "COUPON_ID", nullable = false)
  private Coupon coupon;

  @Comment("쿠폰 사용여부")
  @Column(name = "COUPON_USE_YN", nullable = false)
  private Boolean isUsed = false;

  @Comment("쿠폰 사용일")
  @Column(name = "COUPON_USE_DE")
  private Timestamp usedAt;

  public static CustomerCoupon from(Account account, Coupon coupon) {
    return CustomerCoupon.builder()
        .account(account)
        .coupon(coupon)
        .isUsed(false)
        .usedAt(null)
        .build();
  }
}
