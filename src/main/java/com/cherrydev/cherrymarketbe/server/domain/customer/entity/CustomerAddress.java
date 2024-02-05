package com.cherrydev.cherrymarketbe.server.domain.customer.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
@Entity
@Getter
@Builder
@Comment("고객 주소")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CSTMR_ADRES")
public class CustomerAddress extends BaseEntity {

    @Id
    @Comment("고객 주소 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTMR_ADRES_ID", nullable = false)
    private Long id;

    @Comment("고객 ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ACNT_ID", nullable = false)
    private Account account;

    @Comment("기본 주소 여부")
    @Column(name = "CSTMR_ADRES_BASS_YN", nullable = false)
    private Boolean isDefault = false;

    @Comment("주소명")
    @Column(name = "CSTMR_ADRES_NM", length = 20)
    private String name;

    @Comment("우편번호")
    @Column(name = "CSTMR_ADRES_ZIP", nullable = false, length = 10)
    private String zipCode;

    @Comment("도로명 주소")
    @Column(name = "CSTMR_ADRES_RDNMADR", nullable = false, length = 100)
    private String roadNameAddress;

    @Comment("상세 주소")
    @Column(name = "CSTMR_ADRES_DETAIL", nullable = false, length = 100)
    private String addressDetail;

    public void updateDefaultAddress(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void updateRoadNameAddress(String roadNameAddress) {
        this.roadNameAddress = roadNameAddress;
    }

    public void updateAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }



    public static CustomerAddress of(RequestAddAddress request, Account account) {
        return CustomerAddress.builder()
                .account(account)
                .isDefault(request.isDefault())
                .name(request.name())
                .zipCode(request.zipcode())
                .roadNameAddress(request.address())
                .addressDetail(request.addressDetail())
                .build();
    }
}
