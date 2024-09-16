package com.cherrydev.cherrymarketbe.server.domain.payment.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment.PaymentStatus;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Comment("결제 상세")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PYMNT_DETAIL")
public class PaymentDetail extends BaseEntity {

  @Id
  @Comment("결제 상세 ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PYMNT_ID", nullable = false)
  private Long id;

  @Comment("주문 ID")
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ORDERS_ID", nullable = false, referencedColumnName = "ORDERS_ID")
  private Orders orders;

  @Comment("결제 키")
  @Column(name = "PYMNT_CONFM_NUMBER")
  private String paymentKey;

  @Comment("결제 상태")
  @Enumerated(EnumType.STRING)
  @Column(name = "PYMNT_STTUS", nullable = false)
  private PaymentStatus paymentStatus;

  @Comment("결제 수단")
  @Column(name = "PYMNT_MTH", nullable = false)
  private String paymentMethods;

  @Comment("결제 카드 번호")
  @Column(name = "PYMNT_NO")
  private String paymentCardNumber;

  @Comment("결제 카드 발급사")
  @Column(name = "PYMNT_KND")
  private String paymentCardIssuer;

  @Comment("결제 할부")
  @Column(name = "PYMNT_INSTLMT")
  private Long paymentInstallment;

  @Comment("결제 일시")
  @Column(name = "PYMNT_DE", nullable = false)
  private Timestamp paidAt;

  @Comment("결제 금액")
  @Column(name = "PYMNT_AMOUNT")
  private Long paymentAmount;

  @Comment("총 주문 금액")
  @Column(name = "TOTAL_AMOUNT", nullable = false)
  private Long totalAmount;

  @Comment("할인 금액")
  @Column(name = "DSCNT_AMOUNT")
  private Long discountedAmount;

  @Comment("배송비")
  @Column(name = "PYMNT_DLIV_CT")
  private Long deliveryCost;

  @Comment("적립금 사용")
  @Column(name = "USE_REWARD")
  private Long rewardUsed;

  @Comment("쿠폰 사용")
  @Column(name = "USE_COUPON")
  private Long couponUsed;

  @Comment("주문 취소 일시")
  @Column(name = "DELETED_DE")
  private Timestamp deletedAt;

  public static PaymentDetail from(
      Orders orders, Long totalAmount, Long discountAmount, Long paymentAmount, Long rewardUsed) {
    return PaymentDetail.builder()
        .orders(orders)
        .paymentStatus(PaymentStatus.READY)
        .totalAmount(totalAmount)
        .discountedAmount(discountAmount)
        .paymentAmount(paymentAmount)
        .rewardUsed(rewardUsed)
        .build();
  }

  public void applyApprovalInfo(TossPayment tossPayment) {
    this.paymentKey = tossPayment.paymentKey();
    this.paymentStatus = PaymentStatus.DONE;
    this.paymentMethods = tossPayment.method();
    this.paymentCardNumber = tossPayment.card().number();
    this.paymentCardIssuer = tossPayment.card().issuerCode();
    this.paymentInstallment = tossPayment.card().installmentPlanMonths();
    this.paidAt = Timestamp.valueOf(tossPayment.approvedAt());
  }
}
