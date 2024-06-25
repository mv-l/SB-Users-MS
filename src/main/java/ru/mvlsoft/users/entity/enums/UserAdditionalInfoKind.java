package ru.mvlsoft.users.entity.enums;

public enum UserAdditionalInfoKind {
    SOFT_SKILLS("S_SKILLS"), HOBBY("HOBBY");

    private String code;

    private UserAdditionalInfoKind(String code) {
        this.code = code.toUpperCase();
    }

    public String getCode() {
        return code;
    }
}
