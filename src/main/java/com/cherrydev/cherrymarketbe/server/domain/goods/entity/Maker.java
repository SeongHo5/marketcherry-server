package com.cherrydev.cherrymarketbe.server.domain.goods.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Comment("제조사")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "MAKR",
    uniqueConstraints = {
      @UniqueConstraint(name = "MAKR_BIZRNO_UNIQUE", columnNames = "MAKR_BIZRNO"),
      @UniqueConstraint(name = "MAKR_EMAIL_UNIQUE", columnNames = "MAKR_EMAIL")
    })
public class Maker extends BaseEntity {

  @Id
  @Comment("제조사 ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MAKR_ID", nullable = false)
  private Long id;

  @Comment("제조사명")
  @Column(name = "MAKR_NM", nullable = false, length = 20)
  private String name;

  @Comment("제조사 사업자번호")
  @Column(name = "MAKR_BIZRNO", nullable = false, length = 20)
  private String buisnessNumber;

  @Comment("제조사 연락처")
  @Column(name = "MAKR_CTTPC", nullable = false, length = 30)
  private String contact;

  @Comment("제조사 이메일")
  @Column(name = "MAKR_EMAIL", nullable = false, length = 50)
  private String email;
}
