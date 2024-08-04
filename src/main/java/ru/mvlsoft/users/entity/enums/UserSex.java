package ru.mvlsoft.users.entity.enums;

import lombok.Getter;

@Getter
public enum UserSex {
    MALE("M"), FEMALE("F");

    private final String code;

    UserSex(String code) {
        this.code = code.toUpperCase();
    }

}
