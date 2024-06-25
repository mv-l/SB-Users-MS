package ru.mvlsoft.users.unit.entity.enums;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AdditionalInfoKindTest {

    @Test
    void getCode() {
        assertTrue(UserAdditionalInfoKind.HOBBY.getCode().length() != 0);
    }
}
