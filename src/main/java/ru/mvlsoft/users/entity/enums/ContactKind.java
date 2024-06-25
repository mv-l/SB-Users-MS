package ru.mvlsoft.users.entity.enums;

public enum ContactKind {
    PHONE("PHONE"), EMAIL("EMAIL");

    private String code;

    private ContactKind(String code) {
        this.code = code.toUpperCase();
    }

    public String getCode() {
        return code;
    }
}
