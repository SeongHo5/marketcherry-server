package com.cherrydev.cherrymarketbe.server.domain.admin.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.RequestAddDiscount;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Comment("할인")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "DSCNT")
public class Discount extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "DSCNT_ID", nullable = false)
  private Long id;

  @Column(name = "DSCNT_CODE", nullable = false, length = 20)
  private String code;

  @Column(name = "DSCNT_DC", nullable = false, length = 20)
  private String description;

  @Column(name = "DSCNT_RATE", nullable = false)
  private Integer discountRate;

  @Column(name = "DSCNT_BGNDE", nullable = false)
  private LocalDate startDate;

  @Column(name = "DSCNT_ENDDE", nullable = false)
  private LocalDate endDate;

  public static Discount of(RequestAddDiscount request) {
    return Discount.builder()
        .code(request.code())
        .description(request.description())
        .discountRate(request.discountRate())
        .startDate(LocalDate.parse(request.startDate()))
        .endDate(LocalDate.parse(request.endDate()))
        .build();
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public void updateDiscountRate(Integer discountRate) {
    this.discountRate = discountRate;
  }

  public void updateStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public void updateEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }
}
