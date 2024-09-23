package com.cherrydev.cherrymarketbe.server.domain.account.dto.response;

import static com.cherrydev.cherrymarketbe.server.application.common.utils.TimeFormatter.localDateToString;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountInfo {

  String name;
  String email;
  String contact;
  Gender gender;
  String birthdate;

  public static AccountInfo of(Account account) {
    return AccountInfo.builder()
        .name(account.getName())
        .email(account.getEmail())
        .contact(account.getContact())
        .gender(account.getGender())
        .birthdate(localDateToString(account.getBirthdate()))
        .build();
  }
}
