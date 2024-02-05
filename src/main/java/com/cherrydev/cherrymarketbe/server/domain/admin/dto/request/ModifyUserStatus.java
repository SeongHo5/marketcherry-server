package com.cherrydev.cherrymarketbe.server.domain.admin.dto.request;

import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ModifyUserStatus {

    @Email
    @NotNull
    String email;

    @NotNull
    UserStatus newStatus;

    @NotNull
    String restrictedUntil;

    @Builder
    public ModifyUserStatus(String email, String newStatus, String restrictedUntil) {
        this.email = email;
        this.newStatus = UserStatus.valueOf(newStatus);
        this.restrictedUntil = restrictedUntil;
    }

}
