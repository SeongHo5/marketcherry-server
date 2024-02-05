package com.cherrydev.cherrymarketbe.server.domain.customer.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import com.cherrydev.cherrymarketbe.server.domain.customer.enums.RewardGrantType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
@Entity
@Getter
@Builder
@Comment("고객 리워드")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CSTMR_REWARD")
public class CustomerReward extends BaseEntity {

    @Id
    @Comment("고객 리워드 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTMR_REWARD_ID", nullable = false)
    private Long id;

    @Comment("고객 ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ACNT_ID", nullable = false)
    private Account account;

    @Comment("리워드 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "CSTMR_REWARD_TY", nullable = false, length = 20)
    private RewardGrantType grantType;

    @Comment("리워드 금액")
    @Column(name = "CSTMR_REWARD_AMOUNT", nullable = false)
    private Integer rewardAmount;

    @Comment("리워드 적립일")
    @Column(name = "CSTMR_REWARD_ACCML_DE", nullable = false)
    private LocalDate earnedAt;

    @Comment("리워드 만료일")
    @Column(name = "CSTMR_REWARD_END_DE", nullable = false)
    private LocalDate expiredAt;

    @Comment("리워드 사용여부")
    @Column(name = "CSTMR_REWARD_USE_AT", nullable = false)
    private Boolean isUsed = false;

    @Comment("리워드 사용일")
    @Column(name = "CSTMR_REWARD_USE_DE")
    private Timestamp usedAt;

    public static CustomerReward of(RequestAddReward request, Account account) {
        return CustomerReward.builder()
                .account(account)
                .grantType(RewardGrantType.valueOf(request.rewardGrantType()))
                .rewardAmount(request.amounts())
                .earnedAt(LocalDate.parse(request.earnedAt()))
                .expiredAt(LocalDate.parse(request.expiredAt()))
                .build();
    }

}
