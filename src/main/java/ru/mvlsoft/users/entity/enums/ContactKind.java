package ru.mvlsoft.users.entity.enums;

import lombok.Getter;

@Getter
public enum ContactKind {
    PHONE("PHONE"), EMAIL("EMAIL");

    private final String code;

    ContactKind(String code) {
        this.code = code.toUpperCase();
    }

}
