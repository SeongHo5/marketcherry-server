package com.cherrydev.cherrymarketbe.server.domain.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestSignUp {

  @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 한글만 가능합니다.")
  @NotNull String name;

  @Email(message = "유효한 이메일 주소를 입력하세요.")
  @NotNull String email;

  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,20}$",
      message = "비밀번호는 8자에서 20자 사이이며, 특수문자를 포함해야 합니다.")
  @NotNull String password;

  @NotNull String contact;

  @Pattern(regexp = "^(MALE|FEMALE)$", message = "성별 형식이 일치하지 않습니다.")
  @NotNull String gender;

  @Pattern(
      regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12]\\d|3[01])$",
      message = "생년월일 형식이 일치하지 않습니다.")
  @NotNull String birthdate;

  Boolean serviceAgreement;

  Boolean privacyAgreement;

  Boolean marketingAgreement;
}
