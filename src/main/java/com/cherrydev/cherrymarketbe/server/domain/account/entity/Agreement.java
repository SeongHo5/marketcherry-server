package com.cherrydev.cherrymarketbe.server.domain.account.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Comment("고객 서비스 이용 동의 여부")
@Table(name = "CSTMR_STPLAT_AGRE_YN")
public class Agreement extends BaseEntity {

  @Id
  @Comment("서비스 이용 동의 ID")
  @Column(name = "ACNT_ID", nullable = false)
  private Long id;

  @MapsId
  @Comment("고객 계정 ID")
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "ACNT_ID", nullable = false)
  private Account account;

  @Comment("서비스 이용 동의 여부")
  @Column(name = "CSTMR_SVC_USE_STPLAT_AGRE_YN", nullable = false)
  private Boolean termsOfService;

  @Comment("개인정보 처리방침 동의 여부")
  @Column(name = "CSTMR_INDVDLINFO_PROCESS_POILC_AGRE_YN", nullable = false)
  private Boolean privacyPolicy;

  @Comment("마케팅 정보 수신 동의 여부")
  @Column(name = "CSTMR_MARKET_INFO_RECPTN_AGRE_YN")
  private Boolean marketing;

  public static Agreement of(RequestSignUp request) {
    return Agreement.builder()
        .termsOfService(request.getServiceAgreement())
        .privacyPolicy(request.getPrivacyAgreement())
        .marketing(request.getMarketingAgreement())
        .build();
  }
}
