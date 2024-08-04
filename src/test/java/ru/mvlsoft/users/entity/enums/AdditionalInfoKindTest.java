package ru.mvlsoft.users.entity.enums;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdditionalInfoKindTest {

    @Test
    void getCode() {
        assertFalse(UserAdditionalInfoKind.HOBBY.getCode().isEmpty());
    }
}
