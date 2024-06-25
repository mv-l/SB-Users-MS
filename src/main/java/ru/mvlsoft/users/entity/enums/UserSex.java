package ru.mvlsoft.users.entity.enums;

public enum UserSex {
    MALE("M"), FEMALE("F");

    private String code;

    private UserSex(String code) {
        this.code = code.toUpperCase();
    }

    public String getCode() {
        return code;
    }
}
