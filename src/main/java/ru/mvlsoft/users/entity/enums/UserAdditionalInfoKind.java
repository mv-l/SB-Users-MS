package ru.mvlsoft.users.entity.enums;

import lombok.Getter;

@Getter
public enum UserAdditionalInfoKind {
    SOFT_SKILLS("S_SKILLS"), HOBBY("HOBBY");

    private final String code;

    UserAdditionalInfoKind(String code) {
        this.code = code.toUpperCase();
    }

}
