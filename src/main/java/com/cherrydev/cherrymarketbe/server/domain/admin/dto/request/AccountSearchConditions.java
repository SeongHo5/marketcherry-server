package com.cherrydev.cherrymarketbe.server.domain.admin.dto.request;

public record AccountSearchConditions(
        String email,
        String name,
        String gender,
        String registerType,
        String status,
        String role,
        String sort
) {
}
