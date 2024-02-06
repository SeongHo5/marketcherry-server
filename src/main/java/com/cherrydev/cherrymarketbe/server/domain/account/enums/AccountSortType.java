package com.cherrydev.cherrymarketbe.server.domain.account.enums;

public enum AccountSortType {
    NAME_ASC, NAME_DESC, EMAIL_ASC, EMAIL_DESC, REGISTER_DATE_ASC, REGISTER_DATE_DESC;

    public boolean isEquals(String sort) {
        return this.name().equals(sort.toUpperCase());
    }
}
