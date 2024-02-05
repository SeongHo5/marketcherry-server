package com.cherrydev.cherrymarketbe.server.domain.account.enums;

public enum UserRole {

    ROLE_CUSTOMER(Authority.ROLE_CUSTOMER),
    ROLE_SELLER(Authority.ROLE_SELLER),
    ROLE_MANAGER(Authority.ROLE_MANAGER),
    ROLE_ADMIN(Authority.ROLE_ADMIN);

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    private static class Authority {
        public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
        public static final String ROLE_SELLER = "ROLE_SELLER";
        public static final String ROLE_MANAGER = "ROLE_MANAGER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}
