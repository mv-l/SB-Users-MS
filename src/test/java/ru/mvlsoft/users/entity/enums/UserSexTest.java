package ru.mvlsoft.users.entity.enums;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.enums.UserSex;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserSexTest {

    @Test
    void getCode() {
        assertFalse(UserSex.FEMALE.getCode().isEmpty());
    }
}
