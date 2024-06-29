package com.cherrydev.cherrymarketbe.server.domain.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ModifyUserRole {

    @Email
    @NotNull String email;

    @NotNull UserRole newRole;

    @Builder
    public ModifyUserRole(String email, String newRole) {
        this.email = email;
        this.newRole = UserRole.valueOf(newRole);
    }

}
