package com.cherrydev.cherrymarketbe.server.domain.admin.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus;
import lombok.Builder;
import lombok.Value;

import static com.cherrydev.cherrymarketbe.server.application.common.utils.TimeFormatter.timeStampToString;

@Value
@Builder
public class AdminUserInfo {

    Long accountId;
    RegisterType registType;
    String name;
    String email;
    String contact;
    Gender gender;
    String birthdate;
    String createdAt;
    UserRole userRole;
    UserStatus userStatus;

    public static AdminUserInfo of(Account account) {
        return AdminUserInfo.builder()
                .accountId(account.getId())
                .registType(account.getRegistType())
                .name(account.getName())
                .email(account.getEmail())
                .contact(account.getContact())
                .gender(account.getGender())
                .birthdate(account.getBirthdate().toString())
                .createdAt(timeStampToString(account.getCreatedAt()))
                .userRole(account.getUserRole())
                .userStatus(account.getUserStatus())
                .build();
    }


}
