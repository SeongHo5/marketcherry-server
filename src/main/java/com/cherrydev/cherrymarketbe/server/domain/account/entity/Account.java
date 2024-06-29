package com.cherrydev.cherrymarketbe.server.domain.account.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import jakarta.persistence.*;

import lombok.*;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;

import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole.ROLE_CUSTOMER;

@Getter
@Entity
@Builder
@Comment("계정")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ACNT", uniqueConstraints = {
        @UniqueConstraint(name = "ACNT_EMAIL_UNIQUE", columnNames = "ACNT_EMAIL"),
})
@SQLDelete(sql = "UPDATE ACNT SET ACNT_STTUS = 'DELETED', DELETED_DE = CURRENT_TIMESTAMP WHERE ACNT_ID = ?")
public class Account extends BaseEntity {

    @Id
    @Comment("계정 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACNT_ID")
    private Long id;

    @Comment("계정 OAuth ID")
    @Column(name = "ACNT_OAUTH_ID", length = 50)
    private String accountOauthId;

    @Comment("계정 소유자 가입 구분")
    @Enumerated(EnumType.STRING)
    @Column(name = "ACNT_REGIST_TYPE", nullable = false, length = 20)
    private RegisterType registType;

    @Comment("계정 소유자 이름")
    @Column(name = "ACNT_NM", nullable = false, length = 20)
    private String name;

    @Comment("계정 소유자 이메일")
    @Column(name = "ACNT_EMAIL", nullable = false, length = 50)
    private String email;

    @Comment("계정 소유자 비밀번호")
    @Column(name = "ACNT_PASSWORD", nullable = false, length = 200)
    private String password;

    @Comment("계정 소유자 연락처")
    @Column(name = "ACNT_CTTPC", nullable = false, length = 30)
    private String contact;

    @Comment("계정 소유자 성별")
    @Enumerated(EnumType.STRING)
    @Column(name = "ACNT_SEXDSTN", length = 10)
    private Gender gender;

    @Comment("계정 소유자 생년월일")
    @Column(name = "ACNT_BRTH")
    private LocalDate birthdate;

    @Comment("계정 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "ACNT_STTUS", nullable = false, length = 10)
    private UserStatus userStatus;

    @Comment("계정 제한 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "ACNT_ROLE", nullable = false, length = 20)
    private UserRole userRole;

    @Comment("계정 이용 제한 종료일")
    @Column(name = "ACNT_USE_LMTT_DE")
    private LocalDate restrictedUntil;

    @Comment("계정 탈퇴일")
    @Column(name = "DELETED_DE", columnDefinition = "default null")
    private Timestamp deletedAt;

    /**
     * 로컬로 가입 시 계정 생성
     */
    public static Account from(RequestSignUp request, String encodedPassword, RegisterType registerType) {
        return Account.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .contact(request.getContact())
                .gender(Gender.valueOf(request.getGender()))
                .birthdate(LocalDate.parse(request.getBirthdate()))
                .userRole(ROLE_CUSTOMER)
                .registType(registerType)
                .build();
    }

    /**
     * 소셜 로그인 시 계정 생성
     */
    public static Account from(OAuthAccountInfo oAuthAccountInfo, String encodedPassword, RegisterType registerType) {
        return Account.builder()
                .name(oAuthAccountInfo.getName())
                .email(oAuthAccountInfo.getEmail())
                .password(encodedPassword)
                .userRole(ROLE_CUSTOMER)
                .registType(registerType)
                .build();
    }

    public void updatePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public void updateContact(String contact) {
        this.contact = contact;
    }

    public void updateBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    public void updateAccountRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void updateAccountStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void updateRestrictedUntil(LocalDate restrictedUntil) {
        this.restrictedUntil = restrictedUntil;
    }



}
