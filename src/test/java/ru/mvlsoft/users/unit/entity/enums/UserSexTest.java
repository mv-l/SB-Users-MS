package ru.mvlsoft.users.unit.entity.enums;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.enums.UserSex;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserSexTest {

    @Test
    void getCode() {
        assertTrue(UserSex.FEMALE.getCode().length() != 0);
    }
}
