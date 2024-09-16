package com.cherrydev.cherrymarketbe.integration.factory;

import static com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender.MALE;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole.ROLE_CUSTOMER;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus.DELETED;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus.RESTRICTED;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestModifyAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import java.time.LocalDate;

public class AccountFactory {

  public static RequestSignUp createSignUpRequestDtoA() {
    return RequestSignUp.builder()
        .name("김영희")
        .email("test555@marketcherry.com")
        .password("Testuser12#")
        .contact(("010-1234-5678"))
        .gender("FEMALE")
        .birthdate("1990-01-01")
        .serviceAgreement(Boolean.TRUE)
        .privacyAgreement(Boolean.TRUE)
        .marketingAgreement(Boolean.TRUE)
        .build();
  }

  public static RequestSignUp createSignUpRequestDtoB() {
    return RequestSignUp.builder()
        .name("관리자")
        .email("test@example.com")
        .password("Testuser12#")
        .contact(("010-1234-5678"))
        .gender(("MALE"))
        .birthdate("1990-01-01")
        .serviceAgreement(Boolean.TRUE)
        .privacyAgreement(Boolean.TRUE)
        .marketingAgreement(Boolean.TRUE)
        .build();
  }

  public static RequestSignUp createSignUpRequestDtoC() {
    return RequestSignUp.builder()
        .name("김영희")
        .email("test999999@marketcherry.com")
        .password("Testuser12#")
        .contact(("010-1234-5678"))
        .gender("FEMALE")
        .birthdate("1990-01-01")
        .serviceAgreement(Boolean.TRUE)
        .privacyAgreement(Boolean.TRUE)
        .marketingAgreement(Boolean.TRUE)
        .build();
  }

  public static Account createAccountA() {
    return Account.builder()
        .name("김영희")
        .email("test@marketcherry.com")
        .password("Password12#")
        .contact("010-1234-1234")
        .gender(MALE)
        .birthdate(LocalDate.parse("1990-01-01"))
        .userRole(ROLE_CUSTOMER)
        .userStatus(RESTRICTED)
        .build();
  }

  public static Account createAccountB() {
    return Account.builder()
        .name("김영희")
        .email("admin@marketcherry.com")
        .password("Admin12!@")
        .contact("+82 10-1234-1234")
        .gender(MALE)
        .birthdate(LocalDate.parse("1990-01-01"))
        .userRole(ROLE_CUSTOMER)
        .userStatus(DELETED)
        .build();
  }

  public static RequestModifyAccountInfo createModifyAccountInfoRequestDtoA() {
    return RequestModifyAccountInfo.builder()
        .password("Testuser12#")
        .contact("010-1234-5678")
        .birthdate("1990-01-01")
        .build();
  }
}
